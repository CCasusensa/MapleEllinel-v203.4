package net.swordie.ms.world;

import net.swordie.ms.Server;
import net.swordie.ms.client.Account;
import net.swordie.ms.client.Client;
import net.swordie.ms.client.alliance.Alliance;
import net.swordie.ms.client.alliance.AllianceResult;
import net.swordie.ms.client.character.*;
import net.swordie.ms.client.character.commands.AdminCommand;
import net.swordie.ms.client.character.commands.AdminCommands;
import net.swordie.ms.client.character.damage.DamageSkinType;
import net.swordie.ms.client.character.items.*;
import net.swordie.ms.client.character.potential.CharacterPotential;
import net.swordie.ms.client.character.potential.CharacterPotentialMan;
import net.swordie.ms.client.character.quest.Quest;
import net.swordie.ms.client.character.quest.QuestManager;
import net.swordie.ms.client.character.runestones.RuneStone;
import net.swordie.ms.client.character.skills.*;
import net.swordie.ms.client.character.skills.info.AttackInfo;
import net.swordie.ms.client.character.skills.info.ForceAtomInfo;
import net.swordie.ms.client.character.skills.info.MobAttackInfo;
import net.swordie.ms.client.character.skills.info.SkillInfo;
import net.swordie.ms.client.character.skills.temp.TemporaryStatManager;
import net.swordie.ms.client.friend.Friend;
import net.swordie.ms.client.friend.FriendFlag;
import net.swordie.ms.client.friend.FriendType;
import net.swordie.ms.client.friend.result.*;
import net.swordie.ms.client.guild.Guild;
import net.swordie.ms.client.guild.GuildMember;
import net.swordie.ms.client.guild.GuildSkill;
import net.swordie.ms.client.guild.bbs.BBSRecord;
import net.swordie.ms.client.guild.bbs.BBSReply;
import net.swordie.ms.client.guild.bbs.GuildBBSPacket;
import net.swordie.ms.client.guild.bbs.GuildBBSType;
import net.swordie.ms.client.guild.result.GuildResult;
import net.swordie.ms.client.guild.result.GuildType;
import net.swordie.ms.client.jobs.Job;
import net.swordie.ms.client.jobs.JobManager;
import net.swordie.ms.client.jobs.adventurer.Archer;
import net.swordie.ms.client.jobs.adventurer.BeastTamer;
import net.swordie.ms.client.jobs.adventurer.Magician;
import net.swordie.ms.client.jobs.adventurer.Warrior;
import net.swordie.ms.client.jobs.cygnus.BlazeWizard;
import net.swordie.ms.client.jobs.legend.Aran;
import net.swordie.ms.client.jobs.legend.Evan;
import net.swordie.ms.client.jobs.legend.Luminous;
import net.swordie.ms.client.jobs.legend.Shade;
import net.swordie.ms.client.jobs.nova.AngelicBuster;
import net.swordie.ms.client.jobs.nova.Kaiser;
import net.swordie.ms.client.jobs.resistance.BattleMage;
import net.swordie.ms.client.jobs.resistance.WildHunter;
import net.swordie.ms.client.jobs.resistance.WildHunterInfo;
import net.swordie.ms.client.jobs.resistance.Xenon;
import net.swordie.ms.client.jobs.sengoku.Kanna;
import net.swordie.ms.client.party.Party;
import net.swordie.ms.client.party.PartyMember;
import net.swordie.ms.client.party.PartyResult;
import net.swordie.ms.client.party.PartyType;
import net.swordie.ms.client.trunk.*;
import net.swordie.ms.connection.InPacket;
import net.swordie.ms.connection.OutPacket;
import net.swordie.ms.connection.db.DatabaseManager;
import net.swordie.ms.connection.packet.*;
import net.swordie.ms.constants.*;
import net.swordie.ms.enums.*;
import net.swordie.ms.enums.EquipBaseStat;
import net.swordie.ms.enums.InvType;
import net.swordie.ms.handlers.ClientSocket;
import net.swordie.ms.handlers.EventManager;
import net.swordie.ms.handlers.PsychicLock;
import net.swordie.ms.handlers.header.InHeader;
import net.swordie.ms.handlers.header.OutHeader;
import net.swordie.ms.life.*;
import net.swordie.ms.life.drop.Drop;
import net.swordie.ms.life.mob.EscortDest;
import net.swordie.ms.life.mob.Mob;
import net.swordie.ms.life.mob.MobStat;
import net.swordie.ms.life.mob.MobTemporaryStat;
import net.swordie.ms.life.mob.skill.MobSkill;
import net.swordie.ms.life.mob.skill.MobSkillID;
import net.swordie.ms.life.mob.skill.MobSkillStat;
import net.swordie.ms.life.movement.Movement;
import net.swordie.ms.life.movement.MovementInfo;
import net.swordie.ms.life.npc.Npc;
import net.swordie.ms.life.npc.NpcMessageType;
import net.swordie.ms.life.pet.Pet;
import net.swordie.ms.life.pet.PetSkill;
import net.swordie.ms.loaders.*;
import net.swordie.ms.scripts.ScriptManagerImpl;
import net.swordie.ms.scripts.ScriptType;
import net.swordie.ms.util.*;
import net.swordie.ms.util.container.Tuple;
import net.swordie.ms.world.field.Field;
import net.swordie.ms.world.field.FieldInstanceType;
import net.swordie.ms.world.field.Foothold;
import net.swordie.ms.world.field.Portal;
import net.swordie.ms.world.field.fieldeffect.FieldEffect;
import net.swordie.ms.world.gach.GachaponConstants;
import net.swordie.ms.world.gach.result.GachaponDlgType;
import net.swordie.ms.world.gach.result.GachaponResult;
import net.swordie.ms.world.shop.NpcShopDlg;
import net.swordie.ms.world.shop.NpcShopItem;
import net.swordie.ms.world.shop.ShopRequestType;
import net.swordie.ms.world.shop.cashshop.CashShop;
import net.swordie.ms.world.shop.result.MsgShopResult;
import net.swordie.ms.world.shop.result.ShopResultType;
import org.apache.log4j.LogManager;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.script.ScriptException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static net.swordie.ms.client.character.skills.temp.CharacterTemporaryStat.*;
import static net.swordie.ms.enums.ChatType.*;
import static net.swordie.ms.enums.EquipBaseStat.*;
import static net.swordie.ms.enums.InvType.*;
import static net.swordie.ms.enums.InventoryOperation.*;
import static net.swordie.ms.enums.Stat.pop;
import static net.swordie.ms.enums.Stat.sp;
import static net.swordie.ms.enums.StealMemoryType.REMOVE_STEAL_MEMORY;
import static net.swordie.ms.enums.StealMemoryType.STEAL_SKILL;

/**
 * Created on 12/14/2017.
 */
public class WorldHandler {
    private static final org.apache.log4j.Logger log = LogManager.getRootLogger();

    public static void handleMigrateIn(Client c, InPacket inPacket) {
        int worldId = inPacket.decodeInt();
        int charId = inPacket.decodeInt();
        byte[] machineID = inPacket.decodeArr(16);
        Tuple<Byte, Client> info = Server.getInstance().getChannelFromTransfer(charId, worldId);
        byte channel = info.getLeft();
        Client oldClient = info.getRight();
        if (!oldClient.hasCorrectMachineID(machineID)) {
            c.write(WvsContext.returnToTitle());
            return;
        }
        c.setMachineID(machineID);
        c.setOldChannel(oldClient.getOldChannel());
        Account acc = oldClient.getAccount();
        c.setAccount(acc);
        Server.getInstance().getWorldById(worldId).getChannelById(channel).removeClientFromTransfer(charId);
        c.setChannel(channel);
        c.setWorldId((byte) worldId);
        c.setChannelInstance(Server.getInstance().getWorldById(worldId).getChannelById(channel));
        Char chr = oldClient.getChr();
        if (chr == null || chr.getId() != charId) {
            chr = acc.getCharById(charId);
        }
        chr.setClient(c);
        chr.setAccount(acc);
        chr.initEquips();
        c.setChr(chr);
        c.getChannelInstance().addChar(chr);
        chr.setJobHandler(JobManager.getJobById(chr.getJob(), chr));
        chr.setFieldInstanceType(FieldInstanceType.CHANNEL);
        Server.getInstance().addAccount(acc);
        acc.setCurrentChr(chr);
        DatabaseManager.saveToDB(acc);
        Field field = chr.getOrCreateFieldByCurrentInstanceType(chr.getFieldID() <= 0 ? 100000000 : chr.getFieldID());
        if (chr.getHP() <= 0) { // automatically revive when relogging
            chr.heal(chr.getMaxHP() / 2);
        }
        if (chr.getPartyID() != 0) {
            Party party = c.getWorld().getPartybyId(chr.getPartyID());
            if (party == null) {
                chr.setPartyID(0);
            } else {
                chr.setParty(party);
            }
        }
        chr.warp(field, true);
        c.write(WvsContext.updateEventNameTag(new int[]{}));
        if (chr.getGuild() != null) {
            chr.setGuild(chr.getClient().getWorld().getGuildByID(chr.getGuild().getId()));
        }
        if (JobConstants.isBeastTamer(chr.getJob())) {
            c.write(CField.beastTamerFuncKeyMappedManInit());
        } else {
            c.write(CField.funcKeyMappedManInit(chr.getFuncKeyMap()));
        }
        chr.setBulletIDForAttack(chr.calculateBulletIDForAttack());
        c.write(WvsContext.friendResult(new LoadFriendResult(chr.getAllFriends())));
        c.write(WvsContext.macroSysDataInit(chr.getMacros()));
        c.write(UserLocal.damageSkinSaveResult(DamageSkinType.DamageSkinSaveReq_SendInfo, null, chr));
        c.write(WvsContext.mapTransferResult(MapTransferType.RegisterListSend, (byte) 5, chr.getHyperRockFields()));
        acc.getMonsterCollection().init(chr);
        chr.checkAndRemoveExpiredItems();
        chr.initBaseStats();
        chr.setOnline(true); // v195+: respect 'invisible login' setting
    }

    public static void handleUserMove(Client c, InPacket inPacket) {
        // CVecCtrlUser::EndUpdateActive
        byte fieldKey = inPacket.decodeByte();
        inPacket.decodeInt(); // ? something with field
        inPacket.decodeInt(); // tick
        inPacket.decodeByte(); // ? doesn't get set at all
        Char chr = c.getChr();
        // CMovePathCommon::Encode
        MovementInfo movementInfo = new MovementInfo(inPacket);
        movementInfo.applyTo(chr);
        chr.getField().checkCharInAffectedAreas(chr);
        chr.getField().broadcastPacket(UserRemote.move(chr, movementInfo), chr);
    }

    public static void handleSummonedMove(Char chr, InPacket inPacket) {
        // CVecCtrlSummoned::EndUpdateActive
        int summonID = inPacket.decodeInt();
        Life life = chr.getField().getLifeByObjectID(summonID);
        if (life instanceof Summon) {
            Summon summon = (Summon) life;
            MovementInfo movementInfo = new MovementInfo(inPacket);
            movementInfo.applyTo(summon);
            chr.getField().broadcastPacket(Summoned.summonedMove(chr.getId(), summonID, movementInfo), chr);
        }
    }

    public static void handleUserChat(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        inPacket.decodeInt();
        String msg = inPacket.decodeString();
        if (msg.length() > 0 && msg.charAt(0) == '@') {
            if (msg.equalsIgnoreCase("@check")) {
                WvsContext.dispose(c.getChr());
                Map<BaseStat, Integer> basicStats = chr.getTotalBasicStats();
                StringBuilder sb = new StringBuilder();
                List<BaseStat> sortedList = Arrays.stream(BaseStat.values()).sorted(Comparator.comparing(Enum::toString)).collect(Collectors.toList());
                for (BaseStat bs : sortedList) {
                    sb.append(String.format("%s = %d, ", bs, basicStats.getOrDefault(bs, 0)));
                }
                chr.chatMessage(Mob, String.format("X=%d, Y=%d %n Stats: %s", chr.getPosition().getX(), chr.getPosition().getY(), sb));
                ScriptManagerImpl smi = chr.getScriptManager();
                // all but field
                smi.stop(ScriptType.Portal);
                smi.stop(ScriptType.Npc);
                smi.stop(ScriptType.Reactor);
                smi.stop(ScriptType.Quest);
                smi.stop(ScriptType.Item);

            } else if (msg.equalsIgnoreCase("@save")) {
                DatabaseManager.saveToDB(chr);
            }
        } else if (msg.charAt(0) == AdminCommand.getPrefix()) {
            boolean executed = false;
            String command = msg.split(" ")[0];
            for (Class clazz : AdminCommands.class.getClasses()) {
                if (!(AdminCommand.getPrefix() + clazz.getSimpleName()).equalsIgnoreCase(command)) {
                    continue;
                }
                executed = true;
                String[] split = null;
                try {
                    AdminCommand adminCommand = (AdminCommand) clazz.getConstructor().newInstance();
                    Method method = clazz.getDeclaredMethod("execute", Char.class, String[].class);
                    split = msg.split(" ");
                    method.invoke(adminCommand, c.getChr(), split);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                    e.printStackTrace();
                }
            }
            if (!executed) {
                chr.chatMessage(Expedition, "Unknown command \"" + command + "\"");
            }
        } else {
            chr.getField().broadcastPacket(User.chat(chr.getId(), ChatUserType.User, msg, false, 0, c.getWorldId()));
        }
    }

    public static void handleUserChangeSlotPositionRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        inPacket.decodeInt(); // update tick
        InvType invType = InvType.getInvTypeByVal(inPacket.decodeByte());
        short oldPos = inPacket.decodeShort();
        short newPos = inPacket.decodeShort();
        short quantity = inPacket.decodeShort();
//        log.debug("Equipped old: " + chr.getEquippedInventory());
//        log.debug("Equip old: " + chr.getEquipInventory());
        InvType invTypeFrom = invType == EQUIP ? oldPos < 0 ? EQUIPPED : EQUIP : invType;
        InvType invTypeTo = invType == EQUIP ? newPos < 0 ? EQUIPPED : EQUIP : invType;
        Item item = chr.getInventoryByType(invTypeFrom).getItemBySlot(oldPos);
        if (item == null) {
            chr.dispose();
            return;
        }
        String itemBefore = item.toString();
        if (newPos == 0) { // Drop
            boolean fullDrop;
            Drop drop;
            if (!item.getInvType().isStackable() || quantity >= item.getQuantity() ||
                    ItemConstants.isThrowingStar(item.getItemId()) || ItemConstants.isBullet(item.getItemId())) {
                // Whole item is dropped (equip/stackable items with all their quantity)
                fullDrop = true;
                chr.getInventoryByType(invTypeFrom).removeItem(item);
                item.drop();
                drop = new Drop(-1, item);
            } else {
                // Part of the stack is dropped
                fullDrop = false;
                Item dropItem = ItemData.getItemDeepCopy(item.getItemId());
                dropItem.setQuantity(quantity);
                item.removeQuantity(quantity);
                drop = new Drop(-1, dropItem);
            }
            int x = chr.getPosition().getX();
            int y = chr.getPosition().getY();
            Foothold fh = chr.getField().findFootHoldBelow(new Position(x, y - GameConstants.DROP_HEIGHT));
            chr.getField().drop(drop, chr.getPosition(), new Position(x, fh.getYFromX(x)));
            drop.setCanBePickedUpByPet(false);
            if (fullDrop) {
                c.write(WvsContext.inventoryOperation(true, false, REMOVE,
                        oldPos, newPos, 0, item));
            } else {
                c.write(WvsContext.inventoryOperation(true, false, UPDATE_QUANTITY,
                        oldPos, newPos, 0, item));
            }
        } else {
            Item swapItem = chr.getInventoryByType(invTypeTo).getItemBySlot(newPos);
            if (swapItem != null) {
//                log.debug("SwapItem before: " + swapItem);
            }
            item.setBagIndex(newPos);
            int beforeSizeOn = chr.getEquippedInventory().getItems().size();
            int beforeSize = chr.getEquipInventory().getItems().size();
            if (invType == EQUIP && invTypeFrom != invTypeTo) {
                // TODO: verify job (see item.RequiredJob), level, stat, unique equip requirements
                // before we allow the player to equip this
                if (invTypeFrom == EQUIPPED) {
                    chr.unequip(item);
                } else {
                    chr.equip(item);
                    if (swapItem != null) {
                        chr.unequip(swapItem);
                    }
                }
            }
            if (swapItem != null) {
                swapItem.setBagIndex(oldPos);
//                log.debug("SwapItem after: " + swapItem);
            }
            int afterSizeOn = chr.getEquippedInventory().getItems().size();
            int afterSize = chr.getEquipInventory().getItems().size();
            if (afterSize + afterSizeOn != beforeSize + beforeSizeOn) {
                throw new RuntimeException("Data duplication!");
            }
            c.write(WvsContext.inventoryOperation(true, false, MOVE, oldPos, newPos,
                    0, item));
            item.updateToChar(chr);
//            log.debug("Item before: " + itemBefore);
//            log.debug("Item before: " + item);
        }
//        log.debug("Equipped after: " + chr.getEquippedInventory());
//        log.debug("Equip after: " + chr.getEquipInventory());
        chr.setBulletIDForAttack(chr.calculateBulletIDForAttack());
    }

    private static void handleAttack(Client c, AttackInfo attackInfo) {
        Char chr = c.getChr();
        chr.chatMessage(attackInfo.skillId + "");
        int skillID = attackInfo.skillId;
        if (attackInfo.attackHeader == OutHeader.SUMMONED_ATTACK || chr.checkAndSetSkillCooltime(skillID)
                || chr.hasSkillCDBypass() || SkillConstants.isMultiAttackCooldownSkill(skillID)) {
            byte slv = attackInfo.slv;
            chr.chatMessage(Mob, "SkillID: " + skillID);
            Field field = c.getChr().getField();
            Job sourceJobHandler = chr.getJobHandler();
            SkillInfo si = SkillData.getSkillInfoById(skillID);
            if (si != null && si.isMassSpell() && sourceJobHandler.isBuff(skillID) && chr.getParty() != null) {
                Rect r = si.getFirstRect();
                if (r != null) {
                    Rect rectAround = chr.getRectAround(r);
                    for (PartyMember pm : chr.getParty().getOnlineMembers()) {
                        if (pm.getChr() != null && pm.getChr().getField() == chr.getField()
                                && rectAround.hasPositionInside(pm.getChr().getPosition())) {
                            Char ptChr = pm.getChr();
                            Effect effect = Effect.skillAffected(skillID, slv, 0);
                            if (ptChr != chr) {  // Caster shouldn't get the Affected Skill Effect
                                chr.getField().broadcastPacket(
                                        UserRemote.effect(ptChr.getId(), effect)
                                        , ptChr);
                                ptChr.write(User.effect(effect));
                                sourceJobHandler.handleAttack(c, attackInfo);
                            }

                        }
                    }
                }
            }
            sourceJobHandler.handleAttack(c, attackInfo);
            if (attackInfo.attackHeader != null) {
                switch (attackInfo.attackHeader) {
                    case SUMMONED_ATTACK:
                        chr.getField().broadcastPacket(Summoned.summonedAttack(chr.getId(), attackInfo, false), chr);
                        break;
                    case FAMILIAR_ATTACK:
                        chr.getField().broadcastPacket(CFamiliar.familiarAttack(chr.getId(), attackInfo), chr);
                        break;
                    default:
                        chr.getField().broadcastPacket(UserRemote.attack(chr, attackInfo), chr);
                }
            }
            int multiKillMessage = 0;
            long mobexp = 0;
            for (MobAttackInfo mai : attackInfo.mobAttackInfo) {
                Mob mob = (Mob) field.getLifeByObjectID(mai.mobId);
                if (mob == null) {
                    chr.chatMessage(ChatType.Expedition, String.format("Wrong attack info parse (probably)! SkillID = %d, Mob ID = %d", skillID, mai.mobId));
                } else if (mob.getHp() > 0) {
                    long totalDamage = 0;
                    for (int dmg : mai.damages) {
                        totalDamage += dmg;
                    }
                    mob.damage(chr, totalDamage);
                    if (mob.getHp() < 0) {
                        mob.onKilledByChar(chr);

                        // MultiKill +1,  per killed mob
                        multiKillMessage++;
                        mobexp = mob.getForcedMobStat().getExp();
                    }
                }
            }

            // MultiKill Message Popup & Exp
            if (multiKillMessage > 2) {
                int bonusExpMultiplier = (multiKillMessage - 2) * 5;
                long totalBonusExp = (long) (mobexp * (bonusExpMultiplier * GameConstants.MULTI_KILL_BONUS_EXP_MULTIPLIER));
                chr.write(UserLocal.comboCounter((byte) 0, (int) totalBonusExp, multiKillMessage > 10 ? 10 : multiKillMessage));
                chr.addExpNoMsg(totalBonusExp);
            }
        }
    }


    public static void handleChangeFieldRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        if (inPacket.getUnreadAmount() == 0) {
            // Coming back from the cash shop
//            chr.warp(chr.getOrCreateFieldByCurrentInstanceType(chr.getFieldID()));
            c.getChannelInstance().addClientInTransfer(c.getChannel(), chr.getId(), c);
            c.write(ClientSocket.migrateCommand(true, (short) c.getChannelInstance().getPort()));
            return;
        }
        byte fieldKey = inPacket.decodeByte();
        int targetField = inPacket.decodeInt();
        int x = inPacket.decodeShort();
        int y = inPacket.decodeShort();
        String portalName = inPacket.decodeString();
        if (portalName != null && !"".equals(portalName)) {
            Field field = chr.getField();
            Portal portal = field.getPortalByName(portalName);
            if (portal.getScript() != null && !portal.getScript().equals("")) {
                chr.getScriptManager().startScript(portal.getId(), portal.getScript(), ScriptType.Portal);
            } else {
                Field toField = chr.getOrCreateFieldByCurrentInstanceType(portal.getTargetMapId());
                if (toField == null) {
                    return;
                }
                Portal toPortal = toField.getPortalByName(portal.getTargetPortalName());
                if (toPortal == null) {
                    toPortal = toField.getPortalByName("sp");
                }
                chr.warp(toField, toPortal);
            }
        } else {
            // Character is dead, respawn request
            inPacket.decodeByte(); // always 0
            byte tarfield = inPacket.decodeByte(); // ?
            byte reviveType = inPacket.decodeByte();
            int returnMap = chr.getField().getReturnMap();
            switch (reviveType) {
                // so far only got 0?
            }
            if (!chr.hasBuffProtector()) {
                chr.getTemporaryStatManager().removeAllStats();
            }
            int deathcount = chr.getDeathCount();
            if (deathcount != 0) {
                if (deathcount > 0) {
                    deathcount--;
                    chr.setDeathCount(deathcount);
                    chr.write(UserLocal.deathCountInfo(deathcount));
                }
                chr.warp(chr.getOrCreateFieldByCurrentInstanceType(returnMap));

            } else {
                if (chr.getParty() != null) {
                    chr.getParty().clearFieldInstances(0);
                } else {
                    if (targetField != -1) {// TODO: Add checks for that.
                        chr.warp(chr.getOrCreateFieldByCurrentInstanceType(targetField));
                    } else {
                        chr.warp(chr.getOrCreateFieldByCurrentInstanceType(chr.getField().getForcedReturn()));
                    }
                }
            }
            chr.heal(chr.getMaxHP());
            chr.setBuffProtector(false);
        }
    }

    public static void handleUserPortalScriptRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        byte portalID = inPacket.decodeByte();
        String portalName = inPacket.decodeString();
        Portal portal = chr.getField().getPortalByName(portalName);
        String script = portalName;
        if (portal != null) {
            portalID = (byte) portal.getId();
            script = "".equals(portal.getScript()) ? portalName : portal.getScript();
        }
        chr.getScriptManager().startScript(portalID, script, ScriptType.Portal);
    }

    public static void handleUserPortalScrollUseRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        inPacket.decodeInt(); //tick
        short slot = inPacket.decodeShort();
        int itemID = inPacket.decodeInt();
        ItemInfo ii = ItemData.getItemInfoByID(itemID);
        Field field = chr.getField();
        Field toField;

        if (itemID != 2030000) {
            toField = chr.getOrCreateFieldByCurrentInstanceType(ii.getMoveTo());
        } else {
            toField = chr.getOrCreateFieldByCurrentInstanceType(field.getReturnMap());
        }
        Portal portal = toField.getDefaultPortal();
        chr.warp(toField, portal);
        chr.consumeItem(itemID, 1);
    }

    public static void handleUserSkillUpRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        inPacket.decodeInt(); // tick
        int skillID = inPacket.decodeInt();
        int amount = inPacket.decodeInt();

        if (amount < 1) {
            chr.dispose();
            return;
        }

        Skill skill = chr.getSkill(skillID, true);
        boolean isPassive = SkillConstants.isPassiveSkill(skillID);
        if (isPassive) {
            chr.removeFromBaseStatCache(skill);
        }
        byte jobLevel = (byte) JobConstants.getJobLevel((short) skill.getRootId());
        if (JobConstants.isZero((short) skill.getRootId())) {
            jobLevel = JobConstants.getJobLevelByZeroSkillID(skillID);
        }
        Map<Stat, Object> stats = null;
        if (JobConstants.isExtendSpJob(chr.getJob())) {
            // TODO: get proper sp for beginner jobs
            ExtendSP esp = chr.getAvatarData().getCharacterStat().getExtendSP();
            int currentSp = esp.getSpByJobLevel(jobLevel);
            if (currentSp >= amount) {
                int curLevel = skill.getCurrentLevel();
                int max = skill.getMaxLevel();
                int newLevel = curLevel + amount > max ? max : curLevel + amount;
                skill.setCurrentLevel(newLevel);
                esp.setSpToJobLevel(jobLevel, currentSp - amount);
                stats = new HashMap<>();
                stats.put(sp, esp);
            }
        } else {
            int currentSp = chr.getAvatarData().getCharacterStat().getSp();
            if (currentSp >= amount) {
                int curLevel = skill.getCurrentLevel();
                int max = skill.getMaxLevel();
                int newLevel = curLevel + amount > max ? max : curLevel + amount;
                skill.setCurrentLevel(newLevel);
                chr.getAvatarData().getCharacterStat().setSp(currentSp - amount);
                stats = new HashMap<>();
                stats.put(sp, chr.getAvatarData().getCharacterStat().getSp());
            }
        }
        if (stats != null) {
            c.write(WvsContext.statChanged(stats));
            List<Skill> skills = new ArrayList<>();
            skills.add(skill);
            chr.addSkill(skill);
            if (isPassive) {
                chr.addToBaseStatCache(skill);
            }
            c.write(WvsContext.changeSkillRecordResult(skills, true, false, false, false));
        } else {
            log.error(String.format("skill stats are null (%d)", skillID));
            chr.dispose();
        }
    }

    public static void handleBodyAttack(Client c, InPacket inPacket) {
        AttackInfo ai = new AttackInfo();
        ai.attackHeader = OutHeader.REMOTE_BODY;
        ai.fieldKey = inPacket.decodeByte();
        byte mask = inPacket.decodeByte();
        ai.hits = (byte) (mask & 0xF);
        ai.mobCount = (mask >>> 4) & 0xF;
        ai.skillId = inPacket.decodeInt();
        ai.slv = inPacket.decodeByte();
        inPacket.decodeInt(); // crc
        ai.areaPAD = inPacket.decodeByte() >>> 3;
        byte nul = inPacket.decodeByte(); // encoded as 0
        ai.left = ((inPacket.decodeShort() >>> 15) & 1) != 0;
        ai.attackCount = inPacket.decodeInt();
        ai.attackSpeed = inPacket.decodeByte(); // encoded as 0
        ai.wt = inPacket.decodeInt();
        ai.ar01Mad = inPacket.decodeInt(); // only done if mage skill
        byte idk2 = inPacket.decodeByte();

        if (ai.skillId > 0) {
            for (int i = 0; i < ai.mobCount; i++) {
                MobAttackInfo mai = new MobAttackInfo();
                mai.mobId = inPacket.decodeInt();
                mai.idkInt = inPacket.decodeInt();
                mai.calcDamageStatIndex = inPacket.decodeByte();
                mai.templateID = inPacket.decodeInt();
                mai.rect = new Rect(inPacket.decodePosition(), inPacket.decodePosition());
                mai.idk6 = inPacket.decodeShort();
                mai.hitAction = inPacket.decodeByte();
                int[] damages = new int[ai.hits];
                for (int j = 0; j < ai.hits; j++) {
                    damages[j] = inPacket.decodeInt();
                }
                mai.damages = damages;
                mai.mobUpDownYRange = inPacket.decodeInt();
                inPacket.decodeInt(); // crc
                // Begin PACKETMAKER::MakeAttackInfoPacket
                byte type = inPacket.decodeByte();
                String currentAnimationName = "";
                int animationDeltaL = 0;
                String[] hitPartRunTimes = new String[0];
                if (type == 1) {
                    currentAnimationName = inPacket.decodeString();
                    animationDeltaL = inPacket.decodeInt();
                    int hitPartRunTimesSize = inPacket.decodeInt();
                    hitPartRunTimes = new String[hitPartRunTimesSize];
                    for (int j = 0; j < hitPartRunTimesSize; j++) {
                        hitPartRunTimes[j] = inPacket.decodeString();
                    }
                } else if (type == 2) {
                    currentAnimationName = inPacket.decodeString();
                    animationDeltaL = inPacket.decodeInt();
                }
                // End PACKETMAKER::MakeAttackInfoPacket
                mai.type = type;
                mai.currentAnimationName = currentAnimationName;
                mai.animationDeltaL = animationDeltaL;
                mai.hitPartRunTimes = hitPartRunTimes;
                ai.mobAttackInfo.add(mai);
            }
        }
        ai.pos = inPacket.decodePosition();
        handleAttack(c, ai);
    }

    public static void handleUserAbilityUpRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        if (chr.getStat(Stat.ap) <= 0) {
            return;
        }
        inPacket.decodeInt(); // tick
        short stat = inPacket.decodeShort();
        Stat charStat = Stat.getByVal(stat);
        short amount = 1;
        if (charStat == Stat.mmp || charStat == Stat.mhp) {
            amount = 20;
        }
        chr.addStat(charStat, amount);
        chr.addStat(Stat.ap, (short) -1);
        Map<Stat, Object> stats = new HashMap<>();
        stats.put(charStat, (short) chr.getStat(charStat));
        stats.put(Stat.ap, (short) chr.getStat(Stat.ap));
        c.write(WvsContext.statChanged(stats));
        WvsContext.dispose(chr);
    }

    public static void handleUserAbilityMassUpRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        inPacket.decodeInt(); // tick
        int type = inPacket.decodeInt();
        Stat charStat = null;
        short amount;
        if (type == 1) {
            charStat = Stat.getByVal(inPacket.decodeShort());
        } else if (type == 2) {
            inPacket.decodeInt();
            inPacket.decodeInt();
            inPacket.decodeInt();
            charStat = Stat.getByVal(inPacket.decodeShort());
        }
        inPacket.decodeInt();
        inPacket.decodeShort();
        amount = inPacket.decodeShort();
        short addStat = amount;
        if (chr.getStat(Stat.ap) < amount) {
            return;
        }
        if (charStat == Stat.mmp || charStat == Stat.mhp) {
            addStat *= 20;
        }
        chr.addStat(charStat, addStat);
        chr.addStat(Stat.ap, (short) -amount);
        Map<Stat, Object> stats = new HashMap<>();
        stats.put(charStat, (short) chr.getStat(charStat));
        stats.put(Stat.ap, (short) chr.getStat(Stat.ap));
        c.write(WvsContext.statChanged(stats));
        WvsContext.dispose(chr);
    }

    public static void handleMobApplyCtrl(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        Field field = chr.getField();
        int mobID = inPacket.decodeInt();
        Mob mob = (Mob) field.getLifeByObjectID(mobID);
        c.write(MobPool.changeController(mob, true, true));
    }

    public static void handleMoveMob(Client c, InPacket inPacket) {
        // CMob::GenerateMovePath (line 918 onwards)
        Field field = c.getChr().getField();
        int objectID = inPacket.decodeInt();
        Life life = field.getLifeByObjectID(objectID);
        if (!(life instanceof Mob)) {
            return;
        }
        MobSkillAttackInfo msai = new MobSkillAttackInfo();
        Mob mob = (Mob) life;
        Char controller = field.getLifeToControllers().get(mob);
        byte idk0 = inPacket.decodeByte(); // check if the templateID / 10000 == 250 or 251. No idea for what it's used
        short moveID = inPacket.decodeShort();
        msai.actionAndDirMask = inPacket.decodeByte();
        byte action = inPacket.decodeByte();
        msai.action = (byte) (action >> 1);
        life.setMoveAction(action);
        int skillID = msai.action - 30; // thanks yuuroido :D
        int skillSN = skillID;
        int slv = 0;
        msai.targetInfo = inPacket.decodeInt();
        int afterAttack = -1;
        //c.getChr().chatMessage("" + msai.action);
        boolean didSkill = action != -1;
        if (didSkill && mob.hasSkillDelayExpired() && !mob.isInAttack()) {
            List<MobSkill> skillList = mob.getSkills();
            if (Util.succeedProp(GameConstants.MOB_SKILL_CHANCE)) {
                MobSkill mobSkill;
                mobSkill = skillList.stream()
                        .filter(ms -> ms.getSkillSN() == skillSN
                                /*&& mob.hasSkillOffCooldown(ms.getSkillID(), ms.getLevel())*/)
                        .findFirst()
                        .orElse(null);
                if (mobSkill == null) {
                    skillList = skillList.stream()
                            .filter(ms -> mob.hasSkillOffCooldown(ms.getSkillID(), ms.getLevel()))
                            .collect(Collectors.toList());
                    if (skillList.size() > 0) {
                        mobSkill = skillList.get(Randomizer.nextInt(skillList.size()));
                    }
                }
                didSkill = mobSkill != null;
                if (didSkill) {
                    didSkill = true;
                    skillID = mobSkill.getSkillID();
                    slv = mobSkill.getLevel();
                    MobSkillInfo msi = SkillData.getMobSkillInfoByIdAndLevel(skillID, slv);
                    long curTime = System.currentTimeMillis();
                    long interval = msi.getSkillStatIntValue(MobSkillStat.interval) * 1000;
                    long nextUseableTime = curTime + interval;
                    c.getChr().chatMessage(Mob, String.format("Mob did skill with ID %d (%s), level = %d",
                            mobSkill.getSkillID(), MobSkillID.getMobSkillIDByVal(mobSkill.getSkillID()), mobSkill.getLevel()));
                    mob.putSkillCooldown(skillID, slv, nextUseableTime);
                    if (mobSkill.getSkillAfter() > 0) {
                        mob.getSkillDelays().add(mobSkill);
                        mob.setSkillDelay(mobSkill.getSkillAfter());
                        c.write(MobPool.setSkillDelay(mob.getObjectId(), mobSkill.getSkillAfter(), skillID, slv, 0, null));
                    } else {
                        mobSkill.handleEffect(mob);
                    }
                }
            }
        }
        if (!didSkill) {
            // didn't do a skill, so ensure that the attack gets properly formed
            int attackIdx = skillID + 17;
            if (mob.hasAttackOffCooldown(attackIdx)) {
                MobSkill ms = mob.getAttackById(attackIdx);
                if (ms != null && ms.getAfterAttack() >= 0) {
                    afterAttack = ms.getAfterAttack();
                }
            }
        }
        byte multiTargetForBallSize = inPacket.decodeByte();
        for (int i = 0; i < multiTargetForBallSize; i++) {
            Position pos = inPacket.decodePosition(); // list of ball positions
            msai.multiTargetForBalls.add(pos);
        }

        byte randTimeForAreaAttackSize = inPacket.decodeByte();
        for (int i = 0; i < randTimeForAreaAttackSize; i++) {
            short randTimeForAreaAttack = inPacket.decodeShort(); // could be used for cheat detection, but meh
            msai.randTimeForAreaAttacks.add(randTimeForAreaAttack);
        }

        byte mask = inPacket.decodeByte();
        boolean targetUserIDFromSvr = (mask & 1) != 0;
        boolean isCheatMobMoveRand = ((mask >> 4) & 1) != 0;
        int hackedCode = inPacket.decodeInt();
        int oneTimeActionCS = inPacket.decodeInt();
        int moveActionCS = inPacket.decodeInt();
        int hitExpire = inPacket.decodeInt();
        byte idk = inPacket.decodeByte();
        MovementInfo movementInfo = new MovementInfo(inPacket);
        c.write(MobPool.ctrlAck(mob, true, moveID, skillID, (byte) slv, -1));
        movementInfo.applyTo(mob);
        mob.setInAttack(afterAttack >= 0);
        if (afterAttack >= 0) {
            c.write(MobPool.setAfterAttack(mob.getObjectId(), (short) afterAttack, msai.action, action % 2 != 0));
        }
        field.checkMobInAffectedAreas(mob);
        field.broadcastPacket(MobPool.move(mob, msai, movementInfo), controller);
    }

    public static void handleMobSkillDelayEnd(Char chr, InPacket inPacket) {
        Life life = chr.getField().getLifeByObjectID(inPacket.decodeInt());
        if (!(life instanceof Mob)) {
            return;
        }
        Mob mob = (Mob) life;
        int skillID = inPacket.decodeInt();
        int slv = inPacket.decodeInt();
        int remainCount = 0; // only set in MobDelaySkill::UpdateSequenceMode
        if (inPacket.decodeByte() != 0) {
            remainCount = inPacket.decodeInt();
        }
        List<MobSkill> delays = mob.getSkillDelays();
        MobSkill ms = Util.findWithPred(delays, skill -> skill.getSkillID() == skillID && skill.getLevel() == slv);
        if (ms != null) {
            ms.handleEffect(mob);
        }

    }

    public static void handleUserGrowthRequestHelper(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        Field field = chr.getField();
        short status = inPacket.decodeShort();
        if (status == 0) {
            int mapleGuideMapId = inPacket.decodeInt();
            Field toField = chr.getClient().getChannelInstance().getField(mapleGuideMapId);
            chr.warp(toField);
        }
        if (status == 2) {
            //TODO wtf happens here
            //int write 0
            //int something?
        }

    }

    public static void handleTemporaryStatResetRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        int skillId = inPacket.decodeInt();
        tsm.removeStatsBySkill(skillId);

        if(SkillConstants.isKeyDownSkill(skillId)) {
            chr.getField().broadcastPacket(UserRemote.skillCancel(chr.getId(), skillId), chr);
        }

        if (skillId == net.swordie.ms.client.jobs.resistance.Mechanic.HUMANOID_MECH || skillId == net.swordie.ms.client.jobs.resistance.Mechanic.TANK_MECH) {
            tsm.removeStatsBySkill(skillId + 100); // because of special use
            tsm.sendResetStatPacket(true);
        } else {
            tsm.sendResetStatPacket();
        }

        chr.getJobHandler().handleSkillRemove(c, skillId);
    }

    public static void handleUserStatChangeItemCancelRequest(Char chr, InPacket inPacket) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        int itemID = inPacket.decodeInt();
        tsm.removeStatsBySkill(itemID);
        tsm.sendResetStatPacket();
    }

    public static void handleKeymapUpdateRequest(Client c, InPacket inPacket) {
        inPacket.decodeInt(); // 0
        int size = inPacket.decodeInt();
        for (int i = 0; i < size; i++) {
            int index = inPacket.decodeInt();
            byte type = inPacket.decodeByte();
            int value = inPacket.decodeInt();
            c.getChr().getFuncKeyMap().putKeyBinding(index, type, value);
        }
    }

    public static void handleSummonedAttack(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        Field field = chr.getField();
        AttackInfo ai = new AttackInfo();
        int summonedID = inPacket.decodeInt();
        ai.attackHeader = OutHeader.SUMMONED_ATTACK;
        ai.summon = (Summon) field.getLifeByObjectID(summonedID);
        ai.updateTime = inPacket.decodeInt();
        ai.skillId = inPacket.decodeInt();
        inPacket.decodeInt(); // hardcoded 0
        byte leftAndAction = inPacket.decodeByte();
        ai.attackActionType = (byte) (leftAndAction & 0x7F);
        ai.left = (byte) (leftAndAction >>> 7) != 0;
        byte mask = inPacket.decodeByte();
        ai.hits = (byte) (mask & 0xF);
        ai.mobCount = (mask >>> 4) & 0xF;
        inPacket.decodeByte(); // hardcoded 0
        ai.attackAction = inPacket.decodeShort();
        ai.attackCount = inPacket.decodeShort();
        ai.pos = inPacket.decodePosition();
        inPacket.decodeInt(); // hardcoded -1
        short idk3 = inPacket.decodeShort();
        int idk4 = inPacket.decodeInt();
        inPacket.decodeInt(); // hardcoded 0
        ai.bulletID = inPacket.decodeInt();
        for (int i = 0; i < ai.mobCount; i++) {
            MobAttackInfo mai = new MobAttackInfo();
            mai.mobId = inPacket.decodeInt();
            mai.templateID = inPacket.decodeInt();
            mai.byteIdk1 = inPacket.decodeByte();
            mai.byteIdk2 = inPacket.decodeByte();
            mai.byteIdk3 = inPacket.decodeByte();
            mai.byteIdk4 = inPacket.decodeByte();
            mai.byteIdk5 = inPacket.decodeByte();
            int idk5 = inPacket.decodeInt(); // another template id, same as the one above
            byte byteIdk6 = inPacket.decodeByte();
            mai.rect = inPacket.decodeShortRect();
            short idk6 = inPacket.decodeShort();
            int[] damages = new int[ai.hits];
            for (int j = 0; j < ai.hits; j++) {
                damages[j] = inPacket.decodeInt();
            }
            mai.damages = damages;
            mai.mobUpDownYRange = inPacket.decodeInt();
//            inPacket.decodeInt(); // crc
            // Begin PACKETMAKER::MakeAttackInfoPacket
            byte type = inPacket.decodeByte();
            String currentAnimationName = "";
            int animationDeltaL = 0;
            String[] hitPartRunTimes = new String[0];
            if (type == 1) {
                currentAnimationName = inPacket.decodeString();
                animationDeltaL = inPacket.decodeInt();
                int hitPartRunTimesSize = inPacket.decodeInt();
                hitPartRunTimes = new String[hitPartRunTimesSize];
                for (int j = 0; j < hitPartRunTimesSize; j++) {
                    hitPartRunTimes[j] = inPacket.decodeString();
                }
            } else if (type == 2) {
                currentAnimationName = inPacket.decodeString();
                animationDeltaL = inPacket.decodeInt();
            }
            // End PACKETMAKER::MakeAttackInfoPacket
            mai.type = type;
            mai.currentAnimationName = currentAnimationName;
            mai.animationDeltaL = animationDeltaL;
            mai.hitPartRunTimes = hitPartRunTimes;
            ai.mobAttackInfo.add(mai);
        }
        handleAttack(c, ai);
    }

    public static void handleChangeChannelRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        byte channelId = (byte) (inPacket.decodeByte() + 1);

        Job sourceJobHandler = chr.getJobHandler();
        sourceJobHandler.handleCancelTimer(chr);

        chr.changeChannel(channelId);
    }

    public static void handleUserChangeStatRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        inPacket.decodeInt(); // tick
        int mask = inPacket.decodeInt();
        List<Stat> stats = Stat.getStatsByFlag(mask); // should be in correct order
        inPacket.decodeInt();
        HashMap<Stat, Short> hashMap = new HashMap();
        for (Stat stat : stats) {
            hashMap.put(stat, inPacket.decodeShort()); // always short?
        }
        byte option = inPacket.decodeByte();
        if (hashMap.containsKey(Stat.hp)) {
            chr.heal(hashMap.get(Stat.hp));
        }
        if (hashMap.containsKey(Stat.mp)) {
            chr.healMP(hashMap.get(Stat.mp));
        }
//        c.write(WvsContext.statChanged(newStats));
    }

    public static void handleCreateKinesisPsychicArea(Client c, InPacket inPacket) {
        PsychicArea pa = new PsychicArea();
        pa.action = inPacket.decodeInt();
        pa.actionSpeed = inPacket.decodeInt();
        pa.localPsychicAreaKey = inPacket.decodeInt();
        pa.psychicAreaKey = inPacket.decodeInt();
        pa.skillID = inPacket.decodeInt();
        pa.slv = inPacket.decodeShort();
        pa.duration = inPacket.decodeInt();
        pa.isLeft = inPacket.decodeByte() != 0;
        pa.skeletonFilePathIdx = inPacket.decodeShort();
        pa.skeletonAniIdx = inPacket.decodeShort();
        pa.skeletonLoop = inPacket.decodeShort();
        pa.start = inPacket.decodePositionInt();
        c.write(CField.createPsychicArea(true, pa));
//        AffectedArea aa = new AffectedArea(-1);
//        aa.setSkillID(pa.skillID);
//        aa.setSlv((byte) pa.slv);
//        aa.setMobOrigin((byte) 0);
//        aa.setCharID(c.getChr().getId());
//        int x = pa.start.getX();
//        int y = pa.start.getY();
//        aa.setPosition(new Position(x, y));
//        aa.setFlip(pa.isLeft);
//        aa.setElemAttr(1);
//        aa.setOption(1);
//        SkillInfo si = SkillData.getSkillInfoById(pa.skillID);
//        aa.setRect(aa.getPosition().getRectAround(si.getRects().get(0)));
//        c.getChr().getField().spawnAffectedArea(aa);
    }

    public static void handleReleasePsychicArea(Client c, InPacket inPacket) {
        int localPsychicAreaKey = inPacket.decodeInt();
        c.write(CField.releasePsychicArea(localPsychicAreaKey));
    }

    public static void handleCreatePsychicLock(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        Field f = chr.getField();
        PsychicLock pl = new PsychicLock();
        pl.skillID = inPacket.decodeInt();
        pl.slv = inPacket.decodeShort();
        pl.action = inPacket.decodeInt();
        pl.actionSpeed = inPacket.decodeInt();
        while (inPacket.decodeByte() != 0) {
            PsychicLockBall plb = new PsychicLockBall();
            plb.localKey = inPacket.decodeInt();
            plb.psychicLockKey = inPacket.decodeInt();
            int mobID = inPacket.decodeInt();
            plb.mob = (Mob) f.getLifeByObjectID(mobID);
            plb.stuffID = inPacket.decodeShort();
            plb.usableCount = inPacket.decodeShort();
            plb.posRelID = inPacket.decodeByte();
            plb.start = inPacket.decodePositionInt();
            plb.rel = inPacket.decodePositionInt();
        }
        // TODO can't attack after this, gotta fix
    }

    public static void handleReleasePsychicLock(Client c, InPacket inPacket) {
        int skillID = inPacket.decodeInt();
        short slv = inPacket.decodeShort();
        short count = inPacket.decodeShort();
        int id = inPacket.decodeInt();
        int mobID = inPacket.decodeInt();
        if (mobID != 0) {
            List<Integer> l = new ArrayList<>();
            l.add(mobID);
            c.write(CField.releasePsychicLockMob(l));
        } else {
            c.write(CField.releasePsychicLock(id));
        }
    }

    public static void handleSummonedRemove(Client c, InPacket inPacket) {
        int id = inPacket.decodeInt();

        Char chr = c.getChr();
        Summon summon = (Summon) chr.getField().getLifeByObjectID(id);
        if (summon == null) {
            return;
        }
        if (summon.getSkillID() == BattleMage.CONDEMNATION
                || summon.getSkillID() == BattleMage.CONDEMNATION_I
                || summon.getSkillID() == BattleMage.CONDEMNATION_II
                || summon.getSkillID() == BattleMage.CONDEMNATION_III) {

            ((BattleMage) chr.getJobHandler()).removeCondemnationBuff(summon);
        }

        c.getChr().getField().removeLife(id, false);
    }

    public static void handleForceAtomCollision(Client c, InPacket inPacket) {
        int size = inPacket.decodeInt();
        int idk2 = inPacket.decodeInt();
        for (int i = 0; i < size; i++) {
            int idk3 = inPacket.decodeInt();
            byte idk4 = inPacket.decodeByte();
            int mobID = inPacket.decodeInt();
            Mob mob = (Mob) c.getChr().getField().getLifeByObjectID(mobID);
            if (mob != null) {
//            mob.damage((long) 133337);
//            c.write(CField.damaged(mobID, (long) 133337, mob.getTemplateId(), (byte) 1, (int) mob.getHp(), (int) mob.getMaxHp()));
            }
        }
    }

    public static void handleRequestArrowPlatterObj(Char chr, InPacket inPacket) {
        boolean flip = inPacket.decodeByte() != 0;
        Position position = inPacket.decodePositionInt(); // ignoring this, we just take the char's info we know
        int skillID = Archer.ARROW_PLATTER;
        Skill skill = chr.getSkill(skillID);
        if (skill != null && skill.getCurrentLevel() > 0) {
            Field field = chr.getField();
            Set<FieldAttackObj> currentFaos = field.getFieldAttackObjects();
            // remove the old arrow platter
            currentFaos.stream()
                    .filter(fao -> fao.getOwnerID() == chr.getId() && fao.getTemplateId() == 1)
                    .findAny().ifPresent(field::removeLife);
            SkillInfo si = SkillData.getSkillInfoById(skillID);
            int slv = skill.getCurrentLevel();
            FieldAttackObj fao = new FieldAttackObj(1, chr.getId(), chr.getPosition().deepCopy(), flip);
            field.spawnLife(fao, chr);
            field.broadcastPacket(FieldAttackObjPool.objCreate(fao), chr);
            ScheduledFuture sf = EventManager.addEvent(() -> field.removeLife(fao.getObjectId(), true),
                    si.getValue(SkillStat.u, slv), TimeUnit.SECONDS);
            field.addLifeSchedule(fao, sf);
            field.broadcastPacket(FieldAttackObjPool.setAttack(fao.getObjectId(), 0));
        }
        chr.dispose();
    }

    public static void handleUserCharacterInfoRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        Field field = chr.getField();
        inPacket.decodeInt(); // tick
        int requestID = inPacket.decodeInt();
        Char requestChar = field.getCharByID(requestID);
        if (requestChar == null) {
            chr.chatMessage(SystemNotice, "The character you tried to find could not be found.");
        } else {
            c.write(CField.characterInfo(requestChar));
        }
    }

    public static void handleSummonedHit(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        Field field = chr.getField();

        int summonObjId = inPacket.decodeInt();
        byte attackId = inPacket.decodeByte();
        int damage = inPacket.decodeInt();
        int mobTemplateId = inPacket.decodeInt();
        boolean isLeft = inPacket.decodeByte() != 0;

        Life life = field.getLifeByObjectID(summonObjId);
        if (life == null || !(life instanceof Summon)) {
            return;
        }

        ((Summon) life).onHit(damage, mobTemplateId);
    }

    public static void handleUserFlameOrbRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        int skillID = inPacket.decodeInt();
        byte slv = inPacket.decodeByte();
        short dir = inPacket.decodeShort();
        SkillInfo si = SkillData.getSkillInfoById(skillID);
        int range = si.getValue(SkillStat.range, slv);
        ForceAtomEnum fae;
        switch (skillID) {
            case BlazeWizard.FINAL_ORBITAL_FLAME:
                fae = ForceAtomEnum.ORBITAL_FLAME_4;
                skillID = BlazeWizard.FINAL_ORBITAL_FLAME_ATOM;
                break;
            case BlazeWizard.GRAND_ORBITAL_FLAME:
                fae = ForceAtomEnum.ORBITAL_FLAME_3;
                skillID = BlazeWizard.GRAND_ORBITAL_FLAME_ATOM;
                break;
            case BlazeWizard.GREATER_ORBITAL_FLAME:
                fae = ForceAtomEnum.ORBITAL_FLAME_2;
                skillID = BlazeWizard.GREATER_ORBITAL_FLAME_ATOM;
                break;
            default:
                fae = ForceAtomEnum.ORBITAL_FLAME_1;
                skillID = BlazeWizard.ORBITAL_FLAME_ATOM;
                break;
        }

        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (tsm.hasStatBySkillId(BlazeWizard.ORBITAL_FLAME_RANGE)) {
            range = tsm.getOptByCTSAndSkill(AddRangeOnOff, BlazeWizard.ORBITAL_FLAME_RANGE).nOption;
        }

        int curTime = Util.getCurrentTime();
        int angle = 0; // left
        int firstImpact = 5;
        int secondImpact = 21;
        switch (dir) {
            case 1: // right
                angle = 180;
                break;
            case 2: // up
                angle = 270;
                firstImpact = 11;
                secondImpact = 13;
                range /= 1.5;
                break;
            case 3: // down
                angle = 90;
                firstImpact = 11;
                secondImpact = 13;
                range /= 1.5;
                break;
        }
        ForceAtomInfo fai = new ForceAtomInfo(1, fae.getInc(), firstImpact, secondImpact,
                angle, 0, curTime, si.getValue(SkillStat.mobCount, slv), skillID, new Position(0, 0));
        List<ForceAtomInfo> faiList = new ArrayList<>();
        faiList.add(fai);
        chr.getField().broadcastPacket(CField.createForceAtom(false, 0, chr.getId(), fae.getForceAtomType(), false,
                new ArrayList<>(), skillID, faiList, null, dir, range, null, 0, null));
    }

    public static void handleUserConsumeCashItemUseRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        Inventory cashInv = chr.getInventoryByType(InvType.CASH);
        inPacket.decodeInt(); // tick
        short pos = inPacket.decodeShort();
        int itemID = inPacket.decodeInt();
        Item item = cashInv.getItemBySlot(pos);
        ItemInfo itemInfo = ItemData.getItemInfoByID(itemID);
        if (item == null || item.getItemId() != itemID) {
            return;
        }
        if (itemID / 10000 == 553) {
            // Reward items
            Item reward = itemInfo.getRandomReward();
            chr.addItemToInventory(reward);

        } else if (itemID / 10000 == 539) {
            // Avatar Megaphones
            List<String> lineList = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                String line = inPacket.decodeString();
                lineList.add(line);
            }
            boolean whisperIcon = inPacket.decodeByte() != 0;
            World world = c.getWorld();
            world.broadcastPacket(WvsContext.setAvatarMegaphone(chr, itemID, lineList, whisperIcon));

        } else if (itemID / 10000 == 519) {
            // Pet Skill Items
            long sn = inPacket.decodeLong();
            PetSkill ps = ItemConstants.getPetSkillFromID(itemID);
            if (ps == null) {
                chr.chatMessage(String.format("Unhandled pet skill item %d", itemID));
                return;
            }
            Item pi = chr.getCashInventory().getItemBySN(sn);
            if (!(pi instanceof PetItem)) {
                chr.chatMessage("Could not find that pet.");
                return;
            }
            boolean add = itemID >= 5190014; // add property doesn't include the "Slimming Medicine"
            PetItem petItem = (PetItem) pi;
            if (add) {
                petItem.addPetSkill(ps);
            } else {
                petItem.removePetSkill(ps);
            }
            petItem.updateToChar(chr);
            chr.consumeItem(item);
        } else {

            Equip medal = (Equip) chr.getEquippedInventory().getFirstItemByBodyPart(BodyPart.Medal);
            int medalInt = 0;
            if (medal != null) {
                medalInt = (medal.getAnvilId() == 0 ? medal.getItemId() : medal.getAnvilId()); // Check for Anvilled medal
            }
            String medalString = (medalInt == 0 ? "" : String.format("<%s> ", StringData.getItemStringById(medalInt)));

            switch (itemID) {

                case 5040004: // Hyper Teleport Rock
                    short type = inPacket.decodeShort();
                    if (type == 1) {
                        int fieldId = inPacket.decodeInt();
                        Field field = chr.getOrCreateFieldByCurrentInstanceType(fieldId);
                        chr.warp(field);
                    } else {
                        String targetName = inPacket.decodeString();
                        int worldID = chr.getClient().getChannelInstance().getWorldId();
                        World world = Server.getInstance().getWorldById(worldID);
                        Char targetChr = world.getCharByName(targetName);
                        Position targetPosition = targetChr.getPosition();

                        // Target doesn't exist
                        if (targetChr == null) {
                            chr.chatMessage(String.format("%s is not online.", targetName));

                            // Target is in an instanced Map
                        } else if (targetChr.getFieldInstanceType() != FieldInstanceType.CHANNEL) {
                            chr.chatMessage(String.format("cannot find %s", targetName));

                            // Change channels & warp & teleport
                        } else if (targetChr.getClient().getChannel() != c.getChannel()) {
                            int fieldId = targetChr.getFieldID();
                            chr.changeChannelAndWarp(targetChr.getClient().getChannel(), fieldId);

                            // warp & teleport
                        } else if (targetChr.getFieldID() != chr.getFieldID()) {
                            chr.warp(targetChr.getField());
                            chr.write(CField.teleport(targetPosition, chr));

                            // teleport
                        } else {
                            chr.write(CField.teleport(targetPosition, chr));
                        }
                    }
                    break;

                case ItemConstants.RED_CUBE: // Red Cube
                case ItemConstants.BLACK_CUBE: // Black cube
                    short ePos = (short) inPacket.decodeInt();
                    InvType invType = ePos < 0 ? EQUIPPED : EQUIP;
                    Equip equip = (Equip) chr.getInventoryByType(invType).getItemBySlot(ePos);
                    if (equip == null) {
                        chr.chatMessage(SystemNotice, "Could not find equip.");
                        chr.dispose();
                        return;
                    } else if (equip.getBaseGrade() < ItemGrade.Rare.getVal()) {
                        log.error(String.format("Character %d tried to use cube (id %d) an equip without a potential (id %d)", chr.getId(), itemID, equip.getItemId()));
                        chr.dispose();
                        return;
                    }
                    Equip oldEquip = equip.deepCopy();
                    int tierUpChance = ItemConstants.getTierUpChance(itemID);
                    short hiddenValue = ItemGrade.getHiddenGradeByVal(equip.getBaseGrade()).getVal();
                    boolean tierUp = !(hiddenValue >= ItemGrade.HiddenLegendary.getVal()) && Util.succeedProp(tierUpChance);
                    if (tierUp) {
                        hiddenValue++;
                    }
                    equip.setHiddenOptionBase(hiddenValue, ItemConstants.THIRD_LINE_CHANCE);
                    equip.releaseOptions(false);
                    if (itemID == ItemConstants.RED_CUBE) {
                        c.write(CField.redCubeResult(chr.getId(), tierUp, itemID, ePos, equip));
                        c.write(CField.showItemReleaseEffect(chr.getId(), ePos, false));
                    } else {
                        if (chr.getMemorialCubeInfo() == null) {
                            chr.setMemorialCubeInfo(new MemorialCubeInfo(equip, oldEquip, itemID));
                        }
                        chr.getField().broadcastPacket(User.showItemMemorialEffect(chr.getId(), true, itemID));
                        c.write(WvsContext.blackCubeResult(equip, chr.getMemorialCubeInfo()));
                    }
                    equip.updateToChar(chr);
                    break;
                case ItemConstants.BONUS_POT_CUBE: // Bonus potential cube
                    if (c.getWorld().isReboot()) {
                        log.error(String.format("Character %d attempted to use a bonus potential cube in reboot world.", chr.getId()));
                        chr.dispose();
                        return;
                    }
                    ePos = (short) inPacket.decodeInt();
                    invType = ePos < 0 ? EQUIPPED : EQUIP;
                    equip = (Equip) chr.getInventoryByType(invType).getItemBySlot(ePos);
                    if (equip == null) {
                        chr.chatMessage(SystemNotice, "Could not find equip.");
                        return;
                    } else if (equip.getBonusGrade() < ItemGrade.Rare.getVal()) {
                        log.error(String.format("Character %d tried to use cube (id %d) an equip without a potential (id %d)", chr.getId(), itemID, equip.getItemId()));
                        chr.dispose();
                        return;
                    }
                    tierUpChance = ItemConstants.getTierUpChance(itemID);
                    hiddenValue = ItemGrade.getHiddenGradeByVal(equip.getBonusGrade()).getVal();
                    tierUp = !(hiddenValue >= ItemGrade.HiddenLegendary.getVal()) && Util.succeedProp(tierUpChance);
                    if (tierUp) {
                        hiddenValue++;
                    }
                    equip.setHiddenOptionBonus(hiddenValue, ItemConstants.THIRD_LINE_CHANCE);
                    equip.releaseOptions(true);
                    c.write(CField.inGameCubeResult(chr.getId(), tierUp, itemID, ePos, equip));
                    c.write(CField.showItemReleaseEffect(chr.getId(), ePos, false));
                    equip.updateToChar(chr);
                    break;
                case 5750001: // Nebulite Diffuser
                    ePos = inPacket.decodeShort();
                    equip = (Equip) chr.getEquipInventory().getItemBySlot(ePos);
                    if (equip == null || equip.getSockets()[0] == 0 || equip.getSockets()[0] == ItemConstants.EMPTY_SOCKET_ID) {
                        chr.chatMessage("That item currently does not have an active socket.");
                        chr.dispose();
                        return;
                    }
                    equip.getSockets()[0] = ItemConstants.EMPTY_SOCKET_ID;
                    equip.updateToChar(chr);
                    break;
                case 5072000: // Super Megaphone
                    String text = inPacket.decodeString();
                    boolean whisperIcon = inPacket.decodeByte() != 0;
                    World world = chr.getClient().getWorld();
                    BroadcastMsg smega = BroadcastMsg.megaphone(String.format("%s%s : %s", medalString, chr.getName(), text), (byte) chr.getClient().getChannelInstance().getChannelId(), whisperIcon);
                    world.broadcastPacket(WvsContext.broadcastMsg(smega));
                    break;
                case 5076000: // Item Megaphone
                    text = inPacket.decodeString();
                    whisperIcon = inPacket.decodeByte() != 0;
                    boolean eqpSelected = inPacket.decodeByte() != 0;
                    invType = EQUIP;
                    int itemPosition = 0;
                    if (eqpSelected) {
                        invType = InvType.getInvTypeByVal(inPacket.decodeInt());
                        itemPosition = inPacket.decodeInt();
                        if (invType == EQUIP && itemPosition < 0) {
                            invType = EQUIPPED;
                        }
                    }
                    Item broadcastedItem = chr.getInventoryByType(invType).getItemBySlot((short) itemPosition);

                    world = chr.getClient().getWorld();
                    smega = BroadcastMsg.itemMegaphone(String.format("%s%s : %s", medalString, chr.getName(), text), (byte) chr.getClient().getChannelInstance().getChannelId(), whisperIcon, eqpSelected, broadcastedItem);
                    world.broadcastPacket(WvsContext.broadcastMsg(smega));
                    break;
                case 5077000: // Triple Megaphone
                    byte stringListSize = inPacket.decodeByte();
                    List<String> stringList = new ArrayList<>();
                    for (int i = 0; i < stringListSize; i++) {
                        stringList.add(String.format("%s%s : %s", medalString, chr.getName(), inPacket.decodeString()));
                    }
                    whisperIcon = inPacket.decodeByte() != 0;

                    world = chr.getClient().getWorld();
                    smega = BroadcastMsg.tripleMegaphone(stringList, (byte) chr.getClient().getChannelInstance().getChannelId(), whisperIcon);
                    world.broadcastPacket(WvsContext.broadcastMsg(smega));
                    break;
                case 5062405: // Fusion anvil
                    int appearancePos = inPacket.decodeInt();
                    int functionPos = inPacket.decodeInt();
                    Inventory inv = chr.getEquipInventory();
                    Equip appearance = (Equip) inv.getItemBySlot((short) appearancePos);
                    Equip function = (Equip) inv.getItemBySlot((short) functionPos);
                    if (appearance != null && function != null && appearance.getItemId() / 10000 == function.getItemId() / 10000) {
                        function.getOptions().set(6, appearance.getItemId());
                    }
                    function.updateToChar(chr);
                    break;
                default:
                    chr.chatMessage(Mob, String.format("Cash item %d is not implemented, notify Sjonnie pls.", itemID));
                    return;
            }
        }
        if (itemID != 5040004) {
            chr.consumeItem(item);
        }
        chr.dispose();
    }

    public static void handleUserFinalAttackRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        int skillID = inPacket.decodeInt();
        int pSkill = inPacket.decodeInt();
        int targetID = inPacket.decodeInt();
        int requestTime = inPacket.decodeInt();
        c.write(CField.finalAttackRequest(chr, skillID, chr.getJobHandler().getFinalAttackSkill(), 0, targetID, requestTime));
    }

    public static void handleUserUpgradeAssistItemUseRequest(Client c, InPacket inPacket) {

        Char chr = c.getChr();
        if (c.getWorld().isReboot()) {
            log.error(String.format("Character %d attempted to scroll in reboot world.", chr.getId()));
            chr.dispose();
            return;
        }
        inPacket.decodeInt(); //tick
        short uPos = inPacket.decodeShort(); //Use Position
        short ePos = inPacket.decodeShort(); //Eqp Position
        byte bEnchantSkill = inPacket.decodeByte(); //no clue what this means exactly
//        short idk = inPacket.decodeShort(); //No clue what this is, stayed  00 00  throughout different tests
        Item scroll = chr.getInventoryByType(InvType.CONSUME).getItemBySlot(uPos);
        InvType invType = ePos < 0 ? EQUIPPED : EQUIP;
        Equip equip = (Equip) chr.getInventoryByType(invType).getItemBySlot(ePos);
        if (scroll == null || equip == null) {
            chr.chatMessage(SystemNotice, "Could not find scroll or equip.");
            return;
        }
        int scrollID = scroll.getItemId();
        switch (scrollID) {
            case 2532000: // Safety Scroll
            case 2532001: // Pet Safety Scroll
            case 2532002: // Safety Scroll
            case 2532003: // Safety Scroll
            case 2532004: // Pet Safety Scroll
            case 2532005: // Safety Scroll
                equip.addAttribute(EquipAttribute.UpgradeCountProtection);
                break;
            case 2530000: // Lucky Day
            case 2530002: // Lucky Day
            case 2530003: // Pet Lucky Day
            case 2530004: // Lucky Day
            case 2530006: // Pet Lucky Day
                equip.addAttribute(EquipAttribute.LuckyDay);
                break;
            case 2531000: // Protection Scroll
            case 2531001:
            case 2531004:
            case 2531005:
                equip.addAttribute(EquipAttribute.ProtectionScroll);
                break;
        }
        c.write(CField.showItemUpgradeEffect(chr.getId(), true, false, scrollID, equip.getItemId(), false));
        equip.updateToChar(chr);
        chr.consumeItem(scroll);
    }

    public static void handleUserUpgradeItemUseRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        if (c.getWorld().isReboot()) {
            log.error(String.format("Character %d attempted to scroll in reboot world.", chr.getId()));
            chr.dispose();
            return;
        }
        inPacket.decodeInt(); //tick
        short uPos = inPacket.decodeShort(); //Use Position
        short ePos = inPacket.decodeShort(); //Eqp Position
        byte bEnchantSkill = inPacket.decodeByte(); //no clue what this means exactly
        short idk = inPacket.decodeShort(); //No clue what this is, stayed  00 00  throughout different tests
        Item scroll = chr.getInventoryByType(InvType.CONSUME).getItemBySlot(uPos);
        InvType invType = ePos < 0 ? EQUIPPED : EQUIP;
        Equip equip = (Equip) chr.getInventoryByType(invType).getItemBySlot(ePos);
        if (scroll == null || equip == null || equip.hasSpecialAttribute(EquipSpecialAttribute.Vestige)) {
            chr.chatMessage(SystemNotice, "Could not find scroll or equip.");
            return;
        }
        int scrollID = scroll.getItemId();
        boolean success = true;
        boolean boom = false;
        Map<ScrollStat, Integer> vals = ItemData.getItemInfoByID(scrollID).getScrollStats();
        if (vals.size() > 0) {
            if (equip.getBaseStat(tuc) <= 0) {
                WvsContext.dispose(chr);
                return;
            }
            int chance = vals.getOrDefault(ScrollStat.success, 100);
            int curse = vals.getOrDefault(ScrollStat.cursed, 0);
            success = Util.succeedProp(chance);
            if (success) {
                boolean chaos = vals.containsKey(ScrollStat.randStat);
                if (chaos) {
                    boolean noNegative = vals.containsKey(ScrollStat.noNegative);
                    int max = vals.containsKey(ScrollStat.incRandVol) ? ItemConstants.RAND_CHAOS_MAX : ItemConstants.INC_RAND_CHAOS_MAX;
                    for (EquipBaseStat ebs : ScrollStat.getRandStats()) {
                        int cur = (int) equip.getBaseStat(ebs);
                        if (cur == 0) {
                            continue;
                        }
                        int randStat = Util.getRandom(max);
                        randStat = !noNegative && Util.succeedProp(50) ? -randStat : randStat;
                        equip.addStat(ebs, randStat);
                    }
                } else {
                    for (Map.Entry<ScrollStat, Integer> entry : vals.entrySet()) {
                        ScrollStat ss = entry.getKey();
                        int val = entry.getValue();
                        if (ss.getEquipStat() != null) {
                            equip.addStat(ss.getEquipStat(), val);
                        }
                    }
                }
                equip.addStat(tuc, -1);
                equip.addStat(cuc, 1);
            } else {
                if (curse > 0) {
                    boom = Util.succeedProp(curse);
                    if (boom && !equip.hasAttribute(EquipAttribute.ProtectionScroll)) {
                        chr.consumeItem(equip);
                    } else {
                        boom = false;
                    }
                }
                if (!equip.hasAttribute(EquipAttribute.UpgradeCountProtection)) {
                    equip.addStat(tuc, -1);
                }
            }
            equip.removeAttribute(EquipAttribute.ProtectionScroll);
            equip.removeAttribute(EquipAttribute.LuckyDay);
            equip.removeAttribute(EquipAttribute.UpgradeCountProtection);
            c.write(CField.showItemUpgradeEffect(chr.getId(), success, false, scrollID, equip.getItemId(), boom));
            if (!boom) {
                equip.recalcEnchantmentStats();
                equip.updateToChar(chr);
            }
            chr.consumeItem(scroll);
        } else {
            chr.chatMessage("Could not find scroll data.");
            chr.dispose();
        }
    }

    public static void handleUserItemOptionUpgradeItemUseRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        inPacket.decodeInt(); //tick
        short uPos = inPacket.decodeShort();
        short ePos = inPacket.decodeShort();
        byte bEnchantSkill = inPacket.decodeByte(); // bool or byte?
        Item scroll = chr.getInventoryByType(InvType.CONSUME).getItemBySlot(uPos);
        InvType invType = ePos < 0 ? EQUIPPED : EQUIP;
        Equip equip = (Equip) chr.getInventoryByType(invType).getItemBySlot(ePos);
        if (scroll == null || equip == null) {
            chr.chatMessage(SystemNotice, "Could not find scroll or equip.");
            chr.dispose();
            return;
        } else if (!ItemConstants.canEquipHavePotential(equip)) {
            log.error(String.format("Character %d tried to add potential an eligible item (id %d)", chr.getId(), equip.getItemId()));
            chr.dispose();
            return;
        }
        int scrollID = scroll.getItemId();
        Map<ScrollStat, Integer> vals = ItemData.getItemInfoByID(scrollID).getScrollStats();
        int chance = vals.getOrDefault(ScrollStat.success, 100);
        int curse = vals.getOrDefault(ScrollStat.cursed, 0);
        boolean success = Util.succeedProp(chance);
        if (success) {
            short val;
            int thirdLineChance = ItemConstants.THIRD_LINE_CHANCE;
            switch (scrollID) {
                case 2049400: // Rare Pot
                case 2049401:
                case 2049402:
                case 2049403:
                case 2049404:
                case 2049405:
                case 2049406:
                case 2049407:
                case 2049408:
                case 2049412:
                case 2049413:
                case 2049414:
                case 2049415:
                case 2049416:
                case 2049417:
                case 2049418:
                case 2049419:
                    val = ItemGrade.HiddenRare.getVal();
                    equip.setHiddenOptionBase(val, thirdLineChance);
                    break;
                case 2049700: // Epic pot
                case 2049708:
                    val = ItemGrade.HiddenEpic.getVal();
                    equip.setHiddenOptionBase(val, thirdLineChance);
                    break;
                case 2049762: // Unique Pot
                case 2049764:
                case 2049758:
                    val = ItemGrade.HiddenUnique.getVal();
                    equip.setHiddenOptionBase(val, thirdLineChance);
                    break;

                default:
                    chr.chatMessage(Mob, "Unhandled scroll " + scrollID);
                    chr.dispose();
                    log.error("Unhandled scroll " + scrollID);
                    return;
            }
        }
        c.write(CField.showItemUpgradeEffect(chr.getId(), success, false, scrollID, equip.getItemId(), false));
        equip.updateToChar(chr);
        chr.consumeItem(scroll);
    }

    public static void handleUserItemSlotExtendItemUseRequest(Char chr, InPacket inPacket) {
        inPacket.decodeInt(); // tick
        short uPos = inPacket.decodeShort();
        short ePos = inPacket.decodeShort();
        Item item = chr.getConsumeInventory().getItemBySlot(uPos);
        Item equipItem = chr.getEquipInventory().getItemBySlot(ePos);
        if (item == null || equipItem == null) {
            chr.chatMessage("Could not find either the use item or the equip.");
            return;
        }
        int itemID = item.getItemId();
        Equip equip = (Equip) equipItem;
        int successChance = ItemData.getItemInfoByID(itemID).getScrollStats().getOrDefault(ScrollStat.success, 100);
        boolean success = Util.succeedProp(successChance);
        if (success) {
            switch (itemID) {
                case 2049505: // Gold Potential Stamp
                case 2049517:
                    equip.setOption(2, equip.getRandomOption(false, 2), false);
                    break;
                default:
                    log.error("Unhandled slot extend item " + itemID);
                    chr.chatMessage("Unhandled slot extend item " + itemID);
                    return;
            }
            equip.updateToChar(chr);
        }
        chr.consumeItem(item);
        chr.write(CField.showItemUpgradeEffect(chr.getId(), success, false, itemID, equip.getItemId(), false));
    }

    public static void handleUserAdditionalOptUpgradeItemUseRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        if (c.getWorld().isReboot()) {
            log.error(String.format("Character %d attempted to use bonus potential in reboot world.", chr.getId()));
            chr.dispose();
            return;
        }
        inPacket.decodeInt(); //tick
        short uPos = inPacket.decodeShort();
        short ePos = inPacket.decodeShort();
        byte bEnchantSkill = inPacket.decodeByte();
        Item scroll = chr.getInventoryByType(InvType.CONSUME).getItemBySlot(uPos);
        InvType invType = ePos < 0 ? EQUIPPED : EQUIP;
        Equip equip = (Equip) chr.getInventoryByType(invType).getItemBySlot(ePos);
        if (scroll == null || equip == null) {
            chr.chatMessage(SystemNotice, "Could not find scroll or equip.");
            return;
        }
        int scrollID = scroll.getItemId();
        boolean success;
        Map<ScrollStat, Integer> vals = ItemData.getItemInfoByID(scrollID).getScrollStats();
        int chance = vals.getOrDefault(ScrollStat.success, 100);
        int curse = vals.getOrDefault(ScrollStat.cursed, 0);
        success = Util.succeedProp(chance);
        if (success) {
            short val;
            int thirdLineChance = ItemConstants.THIRD_LINE_CHANCE;
            switch (scrollID) {
                case 2048305: // Bonus Pot
                case 2048308:
                case 2048309:
                case 2048310:
                case 2048311:
                case 2048313:
                case 2048314:
                case 2048316:
                case 2048329:
                    val = ItemGrade.HiddenRare.getVal();
                    equip.setHiddenOptionBonus(val, thirdLineChance);
                    break;
                case 2048306: // Special Bonus Pot
                case 2048307:
                case 2048315:
                case 2048331:
                    val = ItemGrade.HiddenRare.getVal();
                    equip.setHiddenOptionBonus(val, 100);
                    break;
                default:
                    chr.chatMessage(Mob, "Unhandled scroll " + scrollID);
                    break;
            }
        }
        c.write(CField.showItemUpgradeEffect(chr.getId(), success, false, scrollID, equip.getItemId(), false));
        equip.updateToChar(chr);
        chr.consumeItem(scroll);
    }

    public static void handleUserItemReleaseRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        inPacket.decodeInt(); //tick
        short uPos = inPacket.decodeShort();
        short ePos = inPacket.decodeShort();
        Item item = chr.getInventoryByType(InvType.CONSUME).getItemBySlot(uPos); // old system with magnifying glasses
        InvType invType = ePos < 0 ? EQUIPPED : EQUIP;
        Equip equip = (Equip) chr.getInventoryByType(invType).getItemBySlot(ePos);
        if (equip == null) {
            chr.chatMessage(SystemNotice, "Could not find equip.");
            return;
        }
        boolean base = equip.getOptionBase(0) < 0;
        boolean bonus = equip.getOptionBonus(0) < 0;
        if (base && bonus) {
            equip.releaseOptions(true);
            equip.releaseOptions(false);
        } else {
            equip.releaseOptions(bonus);
        }
        c.write(CField.showItemReleaseEffect(chr.getId(), ePos, bonus));
        equip.updateToChar(chr);
    }

    public static void handleUserActiveNickItem(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        int nickItem = inPacket.decodeInt();
        chr.setNickItem(nickItem);
        chr.getField().broadcastPacket(UserRemote.setActiveNickItem(chr), chr);
    }


    public static void handleLikePoint(Client c, InPacket inPacket) {
        //TODO
    }

    public static void handleUserActivateDamageSkin(Client c, InPacket inPacket) {
        int damageSkin = inPacket.decodeInt();
        Char chr = c.getChr();
        chr.setDamageSkin(damageSkin);
//        c.write(User.setDamageSkin(chr));
    }


    public static void handleUserActivateDamageSkinPremium(Client c, InPacket inPacket) {
        int damageSkin = inPacket.decodeInt();
        Char chr = c.getChr();
        chr.setPremiumDamageSkin(damageSkin);
        c.write(User.setPremiumDamageSkin(chr));
    }

    public static void handleEventUiReq(Client c, InPacket inPacket) {
        //TODO: get opcodes for CUIContext::OnPacket
    }

    public static void handlePartyInvitableSet(Client c, InPacket inPacket) {
        c.getChr().setPartyInvitable(inPacket.decodeByte() != 0);
    }

    public static void handleZeroTag(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        int newTF = inPacket.decodeInt();
        int oldTF = inPacket.decodeInt();
        chr.swapZeroState();
    }

    public static void handleRequestSetBlessOfDarkness(Client c, InPacket inPacket) {
        Luminous.changeBlackBlessingCount(c, true); // Increment
    }

    public static void handleBattleRecordOnOffRequest(Client c, InPacket inPacket) {
        // CBattleRecordMan::RequestOnCalc
        boolean on = inPacket.decodeByte() != 0;
        boolean isNew = inPacket.decodeByte() != 0;
        boolean clear = inPacket.decodeByte() != 0;
        c.getChr().setBattleRecordOn(on);
        c.write(BattleRecordMan.serverOnCalcRequestResult(on));
    }

    public static void handleBanMapByMob(Client c, InPacket inPacket) {
        Field field = c.getChr().getField();
        int mobID = inPacket.decodeInt();
        Life life = field.getLifeByTemplateId(mobID);
        if (!(life instanceof Mob)) {
            return;
        }
        Mob mob = (Mob) life;
        Char chr = c.getChr();
        if (mob.isBanMap()) {
            if (mob.getBanType() == 1) {
                if (mob.getBanMsgType() == 1) { // found 2 types (1(most of ban types), 2).
                    String banMsg = mob.getBanMsg();
                    if (banMsg != null && !banMsg.equals("")) {
                        chr.write(WvsContext.message(MessageType.SYSTEM_MESSAGE, 0, banMsg, (byte) 0));
                    }
                }
                Tuple<Integer, String> banMapField = mob.getBanMapFields().get(0);
                if (banMapField != null) {
                    Field toField = chr.getOrCreateFieldByCurrentInstanceType(banMapField.getLeft());
                    if (toField == null) {
                        return;
                    }
                    Portal toPortal = toField.getPortalByName(banMapField.getRight());
                    if (toPortal == null) {
                        toPortal = toField.getPortalByName("sp");
                    }

                    chr.warp(toField, toPortal);
                }
            }
        }
    }

    public static void handleUserSelectNpc(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        int npcID = inPacket.decodeInt();
        Position playerPos = inPacket.decodePosition();
        Life life = chr.getField().getLifeByObjectID(npcID);
        if (!(life instanceof Npc)) {
            chr.chatMessage("Could not find that npc.");
            return;
        }
        Npc npc = (Npc) life;
        int templateID = npc.getTemplateId();
        if (npc.getTrunkGet() > 0 || npc.getTrunkPut() > 0) {
            chr.write(CField.trunkDlg(new TrunkOpen(templateID, chr.getAccount().getTrunk())));
            return;
        }
        String script = npc.getScripts().get(0);
        if (script == null) {
            NpcShopDlg nsd = NpcData.getShopById(templateID);
            if (nsd != null) {
                chr.getScriptManager().stop(ScriptType.Npc); // reset contents before opening shop?
                chr.setShop(nsd);
                chr.write(ShopDlg.openShop(0, nsd));
                chr.chatMessage(String.format("Opening shop %s", npc.getTemplateId()));
                return;
            } else {
                script = String.valueOf(npc.getTemplateId());
            }
        }
        chr.getScriptManager().startScript(npc.getTemplateId(), npcID, script, ScriptType.Npc);
    }

    public static void handleUserScriptMessageAnswer(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        ScriptManagerImpl smi = chr.getScriptManager();
        byte lastType = inPacket.decodeByte();
        NpcMessageType nmt = smi.getNpcScriptInfo().getMessageType();
        if (nmt == null) {
            nmt = lastType < NpcMessageType.values().length ?
                    Arrays.stream(NpcMessageType.values()).filter(n -> n.getVal() == lastType).findAny().orElse(NpcMessageType.None) :
                    NpcMessageType.None;
        }
        if (nmt != NpcMessageType.Monologue) {
            byte action = inPacket.decodeByte();
            int answer = 0;
            boolean hasAnswer = false;
            String ans = null;
            if (nmt == NpcMessageType.AskIngameDirection) {
                InGameDirectionAsk answerType = InGameDirectionAsk.getByVal(action);
                if (answerType == null || answerType == InGameDirectionAsk.NOT) {
                    return;
                }
                boolean success = inPacket.decodeByte() == 1;// bSuccess
                if (answerType == InGameDirectionAsk.CAMERA_MOVE_TIME && success) {
                    int answerVal = inPacket.decodeInt();
                    chr.write(UserLocal.inGameDirectionEvent(InGameDirectionEvent.delay(answerVal)));
                    return;
                }
                chr.getScriptManager().handleAction(nmt, (byte) 1, answerType.getVal());
                return;
            }
            if (nmt == NpcMessageType.AskText && action == 1) {
                ans = inPacket.decodeString();
            } else if (inPacket.getUnreadAmount() >= 4) {
                answer = inPacket.decodeInt();
                hasAnswer = true;
            }
            if (nmt == NpcMessageType.AskAvatar || nmt == NpcMessageType.AskAvatarZero) {
                inPacket.decodeByte();
                hasAnswer = inPacket.decodeByte() != 0;
                answer = inPacket.decodeByte();
            }
            if (nmt == NpcMessageType.AskText && action != 0) {
                chr.getScriptManager().handleAction(nmt, action, ans);
            } else if ((nmt != NpcMessageType.AskNumber && nmt != NpcMessageType.AskMenu &&
                    nmt != NpcMessageType.AskAvatar && nmt != NpcMessageType.AskAvatarZero &&
                    nmt != NpcMessageType.AskSlideMenu) || hasAnswer) {
                // else -> User pressed escape in a selection (choice) screen
                chr.getScriptManager().handleAction(nmt, action, answer);
            } else {
                // User pressed escape in a selection (choice) screen
                chr.getScriptManager().dispose(false);
            }
        } else {
            chr.getScriptManager().handleAction(nmt, (byte) 1, 1); // Doesn't use  response nor answer
        }
    }

    public static void handleDropPickUpRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        byte fieldKey = inPacket.decodeByte();
        inPacket.decodeInt(); // tick
        Position pos = inPacket.decodePosition();
        int dropID = inPacket.decodeInt();
        inPacket.decodeInt(); // CliCrc
        // rest is some info about foreground info, not interested
        Field field = chr.getField();
        Life life = field.getLifeByObjectID(dropID);
        if (life instanceof Drop) {
            Drop drop = (Drop) life;
            boolean success = ((!c.getWorld().isReboot() && drop.getOwnerID() == chr.getId()) || drop.canBePickedUpBy(chr)) && chr.addDrop(drop);
            if (success) {
                field.removeDrop(dropID, chr.getId(), false, -1);
            } else {
                chr.dispose();
            }
        }

    }

    public static void handleUserSitRequest(Char chr, InPacket inPacket) {
        Field field = chr.getField();
        int fieldSeatId = inPacket.decodeShort();

        chr.setPortableChairID(0);
        chr.setPortableChairMsg("");
        chr.write(CField.sitResult(chr.getId(), fieldSeatId));
        field.broadcastPacket(UserRemote.remoteSetActivePortableChair(chr.getId(), 0, false, ""));
        chr.dispose();
    }

    public static void handleUserPortableChairSitRequest(Char chr, InPacket inpacket) {
        Field field = chr.getField();
        int itemId = inpacket.decodeInt(); // item id
        int pos = inpacket.decodeInt(); // setup position
        byte chairBag = inpacket.decodeByte(); // is Chair in a bag
        boolean textChair = inpacket.decodeInt() != 0; // boolean to show text
        String text = "";
        if (textChair) {
            text = inpacket.decodeString(); // text to display
        }

        // Tower Chair  check & id
        inpacket.decodeInt(); // encodes 0
        int unknown = inpacket.decodeInt();

        inpacket.decodeInt(); // Time

        field.broadcastPacket(UserRemote.remoteSetActivePortableChair(chr.getId(), itemId, textChair, text));
        chr.setPortableChairID(itemId);
        chr.setPortableChairMsg(text);
        chr.dispose();
    }

    public static void handleUserDropMoneyRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        inPacket.decodeInt(); // tick
        int amount = inPacket.decodeInt();
        if (chr.getMoney() > amount) {
            chr.deductMoney(amount);
            Drop drop = new Drop(-1, amount);
            drop.setCanBePickedUpByPet(false);
            chr.getField().drop(drop, chr.getPosition());
        }
    }

    public static void handleUserStatChangeItemUseRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        inPacket.decodeInt(); // tick
        short slot = inPacket.decodeShort();
        int itemID = inPacket.decodeInt();
        Item item = chr.getConsumeInventory().getItemBySlot(slot);
        if (item == null || item.getItemId() != itemID) {
            return;
        }
        Map<SpecStat, Integer> specStats = ItemData.getItemInfoByID(itemID).getSpecStats();
        if (specStats.size() > 0) {
            ItemBuffs.giveItemBuffsFromItemID(chr, tsm, itemID);
            chr.consumeItem(item);
        } else {
            switch (itemID) {
                case 2050004: // All cure
                    tsm.removeAllDebuffs();
                    break;
                default:
                    chr.chatMessage(Mob, String.format("Unhandled stat change item %d", itemID));
            }
            chr.consumeItem(item);
        }
        chr.dispose();
    }

    public static void handleUserGatherItemRequest(Client c, InPacket inPacket) {
        inPacket.decodeInt(); // tick
        InvType invType = InvType.getInvTypeByVal(inPacket.decodeByte());
        Char chr = c.getChr();
        Inventory inv = chr.getInventoryByType(invType);
        List<Item> items = inv.getItems();
        items.sort(Comparator.comparingInt(Item::getBagIndex));
        for (Item item : items) {
            int firstSlot = inv.getFirstOpenSlot();
            if (firstSlot < item.getBagIndex()) {
                short oldPos = (short) item.getBagIndex();
                item.setBagIndex(firstSlot);
                chr.write(WvsContext.inventoryOperation(true, false, InventoryOperation.MOVE,
                        oldPos, (short) item.getBagIndex(), 0, item));
            }

        }
        c.write(WvsContext.gatherItemResult(invType.getVal()));
        chr.dispose();
    }

    public static void handleUserSortItemRequest(Client c, InPacket inPacket) {
        inPacket.decodeInt(); // tick
        InvType invType = InvType.getInvTypeByVal(inPacket.decodeByte());
        Char chr = c.getChr();
        Inventory inv = chr.getInventoryByType(invType);
        List<Item> items = inv.getItems();
        items.sort(Comparator.comparingInt(Item::getItemId));
        for (Item item : items) {
            if (item.getBagIndex() != items.indexOf(item) + 1) {
                chr.write(WvsContext.inventoryOperation(true, false, InventoryOperation.REMOVE,
                        (short) item.getBagIndex(), (short) 0, -1, item));
            }
        }
        for (Item item : items) {
            int index = items.indexOf(item) + 1;
            if (item.getBagIndex() != index) {
                item.setBagIndex(index);
                chr.write(WvsContext.inventoryOperation(true, false, InventoryOperation.ADD,
                        (short) item.getBagIndex(), (short) 0, -1, item));
            }
        }
        c.write(WvsContext.sortItemResult(invType.getVal()));
        chr.dispose();
    }

    public static void handleUserScriptItemUseRequest(Client c, InPacket inPacket) {
        inPacket.decodeInt(); // tick
        short slot = inPacket.decodeShort();
        int itemID = inPacket.decodeInt();
        int quant = inPacket.decodeInt();
        Char chr = c.getChr();
        Item item = chr.getConsumeInventory().getItemBySlot(slot);
        if (item == null || item.getItemId() != itemID) {
            item = chr.getCashInventory().getItemBySlot(slot);
        }
        if (item == null || item.getItemId() != itemID) {
            chr.dispose();
            return;
        }
        String script = String.valueOf(itemID);
        ItemInfo ii = ItemData.getItemInfoByID(itemID);
        if (ii.getScript() != null && !"".equals(ii.getScript())) {
            script = ii.getScript();
        }
        chr.getScriptManager().startScript(itemID, script, ScriptType.Item);
        chr.dispose();
    }

    public static void handleUserQuestRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        QuestManager qm = chr.getQuestManager();
        byte type = inPacket.decodeByte();
        int questID = 0;
        int npcTemplateID = 0;
        Position position = null;
        QuestType qt = QuestType.getQTFromByte(type);
        boolean success = false;
        if (qt != null) {
            switch (qt) {
                case QuestReq_AcceptQuest: // Quest start
                case QuestReq_CompleteQuest: // Quest end
                case QuestReq_OpeningScript: // Scripted quest start
                case QuestReq_CompleteScript: // Scripted quest end
                    questID = inPacket.decodeInt();
                    npcTemplateID = inPacket.decodeInt();
                    if (inPacket.getUnreadAmount() > 4) {
                        position = inPacket.decodePosition();
                    }
                    break;
                case QuestReq_ResignQuest: //Quest forfeit
                    questID = inPacket.decodeInt();
                    chr.getQuestManager().removeQuest(questID);
                    break;
                case QuestReq_LaterStep:
                    questID = inPacket.decodeInt();
                    break;
                default:
                    log.error(String.format("Unhandled quest request %s!", qt));
                    break;
            }
        }
        if (questID == 0 || qt == null) {
            chr.chatMessage(String.format("Could not find quest %d.", questID));
            return;
        }
        QuestInfo qi = QuestData.getQuestInfoById(questID);
        switch (qt) {
            case QuestReq_AcceptQuest:
                if (qm.canStartQuest(questID)) {
                    qm.addQuest(QuestData.createQuestFromId(questID));
                    success = true;
                }
                break;
            case QuestReq_CompleteQuest:
                if (qm.hasQuestInProgress(questID)) {
                    Quest quest = qm.getQuests().get(questID);
                    if (quest.isComplete(chr)) {
                        qm.completeQuest(questID);
                        success = true;
                    }
                }
                break;
            case QuestReq_OpeningScript:
                String scriptName = qi.getStartScript();
                if (scriptName == null || scriptName.equalsIgnoreCase("")) {
                    scriptName = String.format("%d%s", questID, ScriptManagerImpl.QUEST_START_SCRIPT_END_TAG);
                }
                chr.getScriptManager().startScript(questID, scriptName, ScriptType.Quest);
                break;
            case QuestReq_CompleteScript:
                scriptName = qi.getEndScript();
                if (scriptName == null || scriptName.equalsIgnoreCase("")) {
                    scriptName = String.format("%d%s", questID, ScriptManagerImpl.QUEST_COMPLETE_SCRIPT_END_TAG);
                }
                chr.getScriptManager().startScript(questID, scriptName, ScriptType.Quest);
                break;
            case QuestReq_LaterStep:
                if (qi != null && qi.getTransferField() != 0) {
                    Field field = chr.getOrCreateFieldByCurrentInstanceType(qi.getTransferField());
                    chr.warp(field);
                }
                break;
        }
        if (success) {
            chr.write(UserLocal.questResult(QuestType.QuestRes_Act_Success, questID, npcTemplateID, 0, false));
        }
    }
    public static void handleUserCompleteNpcSpeech(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        QuestManager qm = chr.getQuestManager();
        int questID = inPacket.decodeInt();
        int npcTemplateID = inPacket.decodeInt();
        int speech = inPacket.decodeByte();

        int objectID = inPacket.decodeInt();
        Life life = chr.getField().getLifeByObjectID(objectID);
        if (!(life instanceof Npc)) {
            chr.chatMessage("Could not find that npc.");
            return;
        }
        if (qm.hasQuestInProgress(questID)) {
            QuestInfo qi = QuestData.getQuestInfoById(questID);
            String scriptName = qi.getSpeech().get(speech-1);
            if (scriptName == null || scriptName.equalsIgnoreCase("")) {
                chr.chatMessage("Could not find that speech - quest id " + questID + ", speech " + speech);
            }
            if (scriptName.contains("NpcSpeech=")) {
                if (scriptName.endsWith("/")) {
                    scriptName = scriptName.substring(0, scriptName.length()-1);
                }
                Quest quest = chr.getQuestManager().getQuests().get(questID);
                if (quest != null) {
                    quest.setQrValue(scriptName);
                    chr.write(WvsContext.questRecordExMessage(quest));
                }
            } else {
                chr.getScriptManager().startScript(questID, scriptName, ScriptType.Quest);
            }
        }
    }

    public static void handleRWMultiChargeCancelRequest(Client c, InPacket inPacket) {
        byte unk = inPacket.decodeByte();
        int skillid = inPacket.decodeInt();

        c.write(UserLocal.rwMultiChargeCancelRequest(unk, skillid));
    }

    public static void handleFoxManActionSetUseRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        inPacket.decodeInt(); // tick
        byte skillNumber = inPacket.decodeByte(); //bSkill Number
        //more of the net.swordie.ms.connection.packet, but seems useless
        switch (skillNumber) {
            case 3:
                Kanna.hakuFoxFire(chr);
                break;
            case 4:
                Kanna.hakuHakuBlessing(chr);
                break;
            case 5:
                Kanna.hakuBreathUnseen(chr);
                break;
        }
    }

    public static void handleDirectionNodeCollision(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        if (chr == null || chr.getField() == null) {
            return;
        }
        Field field = chr.getField();

        int node = inPacket.decodeInt();
        List<String> directionNode = field.getDirectionNode(node);
        if (directionNode == null) {
            return;
        }
        String script = directionNode.get(chr.getCurrentDirectionNode(node));
        if (script == null) {
            return;
        }
        chr.increaseCurrentDirectionNode(node);
        chr.getScriptManager().setCurNodeEventEnd(false);
        chr.getScriptManager().startScript(field.getId(), script, ScriptType.Field);
    }

    public static void handleUserEmotion(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        int emotion = inPacket.decodeInt();
        int duration = inPacket.decodeInt();
        boolean byItemOption = inPacket.decodeByte() != 0;
        chr.getField().broadcastPacket(UserRemote.emotion(chr.getId(), emotion, duration, byItemOption), chr);
    }

    public static void handlePartyRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        byte type = inPacket.decodeByte();
        PartyType prt = PartyType.getByVal(type);
        Party party = chr.getParty();
        if (prt == null) {
            log.error(String.format("Unknown party request type %d", type));
            return;
        }
        switch (prt) {
            case PartyReq_CreateNewParty:
                if (party != null) {
                    chr.chatMessage("You are already in a party.");
                    return;
                }
                boolean appliable = inPacket.decodeByte() != 0;
                String name = inPacket.decodeString();
                party = Party.createNewParty(appliable, name, chr.getClient().getWorld());
                party.addPartyMember(chr);
                party.broadcast(WvsContext.partyResult(PartyResult.createNewParty(party)));
                break;
            case PartyReq_WithdrawParty:
                if (party.hasCharAsLeader(chr)) {
                    party.disband();
                } else {
                    PartyMember leaver = party.getPartyMemberByID(chr.getId());
                    party.broadcast(WvsContext.partyResult(PartyResult.withdrawParty(party, leaver, true, false)));
                    party.removePartyMember(leaver);
                    party.updateFull();
                }
                break;
            case PartyReq_InviteParty:
                String invitedName = inPacket.decodeString();
                Char invited = chr.getField().getCharByName(invitedName);
                if (invited == null) {
                    chr.chatMessage("Could not find that player.");
                    return;
                }
                if (party == null) {
                    party = Party.createNewParty(true, "Party with me!", chr.getClient().getWorld());
                    chr.write(WvsContext.partyResult(PartyResult.createNewParty(party)));
                    party.addPartyMember(chr);
                }
                PartyMember inviter = party.getPartyLeader();
                if (!invited.isPartyInvitable()) {
                    chr.chatMessage(SystemNotice, String.format("%s is currently not accepting party invites.", invitedName));
                } else if (invited.getParty() == null) {
                    invited.write(WvsContext.partyResult(PartyResult.applyParty(party, inviter)));
                    chr.chatMessage(SystemNotice, String.format("You invited %s to your party.", invitedName));
                } else {
                    chr.chatMessage(SystemNotice, String.format("%s is already in a party.", invitedName));
                }
                break;
            case PartyReq_KickParty:
                int expelID = inPacket.decodeInt();
                party.expel(expelID);
                break;
            case PartyReq_ChangePartyBoss:
                int newLeaderID = inPacket.decodeInt();
                party.setPartyLeaderID(newLeaderID);
                party.broadcast(WvsContext.partyResult(PartyResult.changePartyBoss(party, newLeaderID)));
                break;
            case PartyReq_ApplyParty:
                int partyID = inPacket.decodeInt();
                party = chr.getField().getChars().stream()
                        .filter(ch -> ch.getParty() != null && ch.getParty().getId() == partyID)
                        .map(Char::getParty)
                        .findFirst()
                        .orElse(null);
                if (party.getApplyingChar() == null) {
                    party.setApplyingChar(chr);
                    party.getPartyLeader().getChr().write(WvsContext.partyResult(PartyResult.inviteIntrusion(party, chr)));
                } else {
                    chr.chatMessage(SystemNotice, "That party already has an applier. Please wait until the applier is accepted or denied.");
                }
                break;
            default:
                log.error(String.format("Unknown party request type %d", type));
                break;
        }
    }

    public static void handlePartyResult(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        byte type = inPacket.decodeByte();
        int partyID = inPacket.decodeInt();
        PartyType pt = PartyType.getByVal(type);
        if (pt == null) {
            log.error(String.format("Unknown party request result type %d", type));
            return;
        }
        switch (pt) {
            case PartyRes_ApplyParty_Accepted:
                Char leader = chr.getField().getChars().stream()
                        .filter(l -> l.getParty() != null
                                && l.getParty().getId() == partyID).findFirst().orElse(null);
                Party party = leader.getParty();
                if (!party.isFull()) {
                    party.addPartyMember(chr);
                    for (Char onChar : party.getOnlineMembers().stream().map(PartyMember::getChr).collect(Collectors.toList())) {
                        onChar.write(WvsContext.partyResult(PartyResult.joinParty(party, chr.getName())));
                        chr.write(UserRemote.receiveHP(onChar));
                    }
                    party.broadcast(UserRemote.receiveHP(chr));
                } else {
                    chr.write(WvsContext.partyResult(PartyResult.msg(PartyType.PartyRes_JoinParty_AlreadyFull)));
                }
                break;
            case PartyRes_ApplyParty_Rejected:
                leader = chr.getField().getChars().stream()
                        .filter(l -> l.getParty() != null &&
                                l.getParty().getId() == partyID).findFirst().orElse(null);
                leader.chatMessage(SystemNotice, String.format("%s has declined your invite.", chr.getName()));
                break;
            case PartyRes_InviteIntrusion_Accepted:
                party = chr.getClient().getWorld().getPartybyId(partyID);
                Char applier = party.getApplyingChar();
                if (applier.getParty() != null) {
                    party.getPartyLeader().getChr().chatMessage(SystemNotice, String.format("%s is already in a party.", applier.getName()));
                } else if (!party.isFull()) {
                    party.addPartyMember(applier);
                    for (Char onChar : party.getOnlineMembers().stream().map(PartyMember::getChr).collect(Collectors.toList())) {
                        onChar.write(WvsContext.partyResult(PartyResult.joinParty(party, applier.getName())));
                    }
                } else {
                    applier.write(WvsContext.partyResult(PartyResult.msg(PartyType.PartyRes_JoinParty_AlreadyFull)));
                }
                party.setApplyingChar(null);
                break;
            case PartyRes_InviteIntrusion_Rejected:
                party = chr.getClient().getWorld().getPartybyId(partyID);
                applier = party.getApplyingChar();
                if (applier != null) {
                    applier.chatMessage(SystemNotice, "Your party apply request has been denied.");
                    party.setApplyingChar(null);
                }
                break;
            default:
                log.error(String.format("Unknown party request result type %d", type));
                break;
        }
    }

    public static void handleWhisper(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        byte type = inPacket.decodeByte();
        inPacket.decodeInt(); // tick
        String destName = inPacket.decodeString();
        Char dest = c.getWorld().getCharByName(destName);
        if (dest == null) {
            c.write(CField.whisper(destName, (byte) 0, false, "", true));
            return;
        }
        switch (type) {
            case 5: // /find command
                break;
            case 6: // whisper
                String msg = inPacket.decodeString();
                dest.write(CField.whisper(chr.getName(), (byte) (c.getChannel() - 1), false, msg, false));
                chr.chatMessage(Whisper, String.format("%s<< %s", dest.getName(), msg));
                break;
        }

    }

    public static void handlePartyMemberCandidateRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        Field field = chr.getField();
        chr.write(WvsContext.partyMemberCandidateResult(field.getChars().stream()
                .filter(ch -> ch.isPartyInvitable() && !ch.equals(chr) && ch.getParty() == null)
                .collect(Collectors.toSet())));
    }

    public static void handlePartyCandidateRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        if (chr.getParty() != null) {
            chr.write(WvsContext.partyCandidateResult(new HashSet<>()));
            return;
        }
        Field field = chr.getField();
        Set<Party> parties = new HashSet<>();
        for (Char ch : field.getChars()) {
            if (ch.getParty() != null && ch.getParty().hasCharAsLeader(ch)) {
                parties.add(ch.getParty());
            }
        }
        chr.write(WvsContext.partyCandidateResult(parties));
    }

    public static void handleGuildRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        byte type = inPacket.decodeByte();
        GuildType grt = GuildType.getTypeByVal(type);
        if (grt == null) {
            log.error(String.format("Unknown guild request %d", type));
            return;
        }
        Guild guild = chr.getGuild();
        switch (grt) {
            case Req_FindGuildByCid: // AcceptJoinRequest:
                int guildID = inPacket.decodeInt();
                guild = chr.getClient().getWorld().getGuildByID(guildID);
                if (guild != null && chr.getGuild() == null) {
                    guild.addMember(chr);
                    guild.broadcast(WvsContext.guildResult(
                            GuildResult.joinGuild(guild, guild.getMemberByCharID(chr.getId()))));
                    chr.write(WvsContext.guildResult(GuildResult.loadGuild(guild)));
                } else {
                    chr.write(WvsContext.guildResult(GuildResult.msg(GuildType.Res_CreateNewGuild_AlreadyJoined)));
                }
                break;
            case Req_CheckGuildName:
                String name = inPacket.decodeString();
                List<Guild> guilds;
                try (Session s = Server.getInstance().getNewDatabaseSession()) {
                    Transaction t = s.beginTransaction();
                    Query q = s.createQuery("FROM Guild WHERE name = ?");
                    q.setParameter(0, name);
                    guilds = q.list();
                    t.commit();
                }
                if (guilds == null || guilds.size() == 0) {
                    guild = new Guild();
                    guild.setLevel(1);
                    guild.setName(name);
                    DatabaseManager.saveToDB(guild);
                    chr.setGuild(guild);
                    guild = chr.getGuild(); // setGuild may change the instance
                    guild.addMember(chr);
                    guild.setWorldID(chr.getClient().getWorldId());
                    chr.write(WvsContext.guildResult(GuildResult.createNewGuild(guild)));
                    chr.deductMoney(5000000);
                } else {
                    chr.write(WvsContext.guildResult(GuildResult.msg(GuildType.Res_CheckGuildName_AlreadyUsed)));
                }
                break;
            case Req_InviteGuild:
                Char invited = chr.getClient().getChannelInstance().getCharByName(inPacket.decodeString());
                if (invited == null) {
                    chr.write(WvsContext.guildResult(GuildResult.msg(GuildType.Res_JoinGuild_UnknownUser)));
                } else {
                    invited.write(WvsContext.guildResult(GuildResult.inviteGuild(chr)));
                }
                break;
            case Req_WithdrawGuild:
                if (guild == null) {
                    return;
                }
                name = chr.getName();
                guild.broadcast(WvsContext.guildResult(GuildResult.leaveGuild(guild, chr.getId(), name, false)));
                guild.removeMember(chr);
                chr.setGuild(null);
                break;
            case Req_KickGuild:
                if (guild == null) {
                    return;
                }
                int expelledID = inPacket.decodeInt();
                String expelledName = inPacket.decodeString();
                GuildMember gm = guild.getMemberByCharID(expelledID);
                Char expelled = gm.getChr();
                guild.broadcast(WvsContext.guildResult(GuildResult.leaveGuild(guild, expelledID, expelledName, true)));
                if (expelled == null) {
                    expelled = Char.getFromDBById(expelledID);
                    guild.removeMember(gm);
                    DatabaseManager.saveToDB(expelled);
                } else {
                    guild.removeMember(gm);
                }
                break;
            case Req_SetMark:
                if (guild == null) {
                    return;
                }
                guild.setMarkBg(inPacket.decodeShort());
                guild.setMarkBgColor(inPacket.decodeByte());
                guild.setMark(inPacket.decodeShort());
                guild.setMarkColor(inPacket.decodeByte());
                chr.write(WvsContext.guildResult(GuildResult.setMark(guild))); // This doesn't actually update the emblem client side
                guild.broadcast(WvsContext.guildResult(GuildResult.loadGuild(guild)));
                break;
            case Req_SetGradeName:
                if (guild == null) {
                    return;
                }
                String[] newNames = new String[guild.getGradeNames().size()];
                for (int i = 0; i < newNames.length; i++) {
                    newNames[i] = inPacket.decodeString();
                }
                guild.setGradeNames(newNames);
                guild.broadcast(WvsContext.guildResult(GuildResult.setGradeName(guild, newNames)));
                break;
            case Req_SetMemberGrade:
                if (guild == null) {
                    return;
                }
                int id = inPacket.decodeInt();
                byte grade = inPacket.decodeByte();
                gm = guild.getMemberByCharID(id);
                gm.setGrade(grade);
                guild.broadcast(WvsContext.guildResult(GuildResult.setMemberGrade(guild, gm)));
                break;
            case Req_SkillLevelSetUp:
                if (guild == null) {
                    return;
                }
                int skillID = inPacket.decodeInt();
                boolean up = inPacket.decodeByte() != 0;
                if (up) {
                    if (!SkillConstants.isGuildContentSkill(skillID) && !SkillConstants.isGuildNoblesseSkill(skillID)) {
                        log.error(String.format("Character %d tried to add an invalid guild skill (%d)", chr.getId(), skillID));
                        chr.write(WvsContext.guildResult(GuildResult.msg(GuildType.Res_UseSkill_Err)));
                        return;
                    }
                    int spentSp = guild.getSpentSp();
                    if (SkillConstants.isGuildContentSkill(skillID)) {
                        if (spentSp >= guild.getLevel() * 2) {
                            log.error(String.format("Character %d tried to add a guild skill without enough sp (spent %d, level %d).",
                                    chr.getId(), spentSp, guild.getLevel()));
                            chr.write(WvsContext.guildResult(GuildResult.msg(GuildType.Res_SetSkill_LevelSet_Unknown)));
                            return;
                        }
                    } else if (guild.getBattleSp() - guild.getSpentBattleSp() <= 0) { // Noblesse
                        log.error(String.format("Character %d tried to add a guild battle skill without enough sp (spent %d).",
                                chr.getId(), guild.getSpentSp()));
                        chr.write(WvsContext.guildResult(GuildResult.msg(GuildType.Res_SetSkill_LevelSet_Unknown)));
                        return;
                    }
                    SkillInfo si = SkillData.getSkillInfoById(skillID);
                    if (spentSp < si.getReqTierPoint()) {
                        log.error(String.format("Character %d tried to add a guild skill without enough sp spent (spent %d, needed %d).",
                                chr.getId(), spentSp, si.getReqTierPoint()));
                        chr.write(WvsContext.guildResult(GuildResult.msg(GuildType.Res_SetSkill_LevelSet_Unknown)));
                        return;
                    }
                    for (Map.Entry<Integer, Integer> entry : si.getReqSkills().entrySet()) {
                        int reqSkillID = entry.getKey();
                        int reqSlv = entry.getValue();
                        GuildSkill gs = guild.getSkillById(skillID);
                        if (gs == null || gs.getLevel() < reqSlv) {
                            log.error(String.format("Character %d tried to add a guild skill without having the required " +
                                            "skill first (req id %d, needed %d, has %d).",
                                    chr.getId(), reqSkillID, reqSlv, gs == null ? 0 : gs.getLevel()));
                            chr.write(WvsContext.guildResult(GuildResult.msg(GuildType.Res_SetSkill_LevelSet_Unknown)));
                            return;
                        }
                    }
                    GuildSkill skill = guild.getSkillById(skillID);
                    if (skill == null) {
                        skill = new GuildSkill();
                        skill.setBuyCharacterName(chr.getName());
                        skill.setExtendCharacterName(chr.getName());
                        skill.setSkillID(skillID);
                        guild.addGuildSkill(skill);
                    }
                    if (skill.getLevel() >= si.getMaxLevel()) {
                        chr.chatMessage("That skill is already at its max level.");
                        chr.dispose();
                        return;
                    }
                    skill.setLevel((short) (skill.getLevel() + 1));
                    guild.broadcast(WvsContext.guildResult(GuildResult.setSkill(guild, skill, chr.getId())));
                } else {
                    GuildSkill gs = guild.getSkillById(skillID);
                    if (gs == null || gs.getLevel() == 0) {
                        log.error(String.format("Character %d tried to decrement a guild skill without that skill existing (id %d).",
                                chr.getId(), skillID));
                        chr.write(WvsContext.guildResult(GuildResult.msg(GuildType.Res_SetSkill_LevelSet_Unknown)));
                        return;
                    }
                    if (guild.getGgp() < GameConstants.GGP_FOR_SKILL_RESET) {
                        log.error(String.format("Character %d tried to decrement a guild skill without having enough GGP (needed %d, has %d).",
                                chr.getId(), GameConstants.GGP_FOR_SKILL_RESET, guild.getGgp()));
                        chr.write(WvsContext.guildResult(GuildResult.msg(GuildType.Res_SetSkill_LevelSet_Unknown)));
                        return;
                    }
                    guild.setGgp(guild.getGgp() - GameConstants.GGP_FOR_SKILL_RESET);
                    gs.setLevel((short) (gs.getLevel() - 1));
                    guild.broadcast(WvsContext.guildResult(GuildResult.setSkill(guild, gs, chr.getId())));
                    guild.broadcast(WvsContext.guildResult(GuildResult.setGgp(guild)));
                }
                break;
            case Req_BattleSkillOpen:
                if (guild == null) {
                    return;
                }
                chr.write(WvsContext.guildResult(GuildResult.battleSkillOpen(guild)));
                break;
            case Req_UseActiveSkill:
                skillID = inPacket.decodeInt();
                // TODO Remove igp
                long usabletime = chr.getSkillCoolTimes().getOrDefault(skillID, Long.MIN_VALUE);
                if (usabletime > System.currentTimeMillis() && !chr.hasSkillCDBypass()) {
                    chr.chatMessage("That skill is still on cooldown.");
                    return;
                }
                GuildSkill gs = guild.getSkillById(skillID);
                if (gs == null || gs.getLevel() == 0) {
                    log.error(String.format("Character %d tried to use a guild skill without having it (id %d).",
                            chr.getId(), skillID));
                    chr.write(WvsContext.guildResult(GuildResult.msg(GuildType.Res_SetSkill_LevelSet_Unknown)));
                    return;
                }
                SkillInfo si = SkillData.getSkillInfoById(skillID);
                chr.getSkillCoolTimes().put(skillID, System.currentTimeMillis() + 1000 * si.getValue(SkillStat.cooltime, gs.getLevel()));
                chr.getJobHandler().handleJoblessBuff(c, inPacket, skillID, (byte) gs.getLevel());
                break;
            default:
                log.error(String.format("Unhandled guild request %s", grt.toString()));
                break;

        }
    }

    public static void handleAllianceRequest(Char chr, InPacket inPacket) {
        byte type = inPacket.decodeByte();
        AllianceType at = AllianceType.getByVal(type);
        if (at == null) {
            log.error(String.format("Unknown alliance request %d", type));
            return;
        }
        Guild guild = chr.getGuild();
        Alliance alliance = guild == null ? null : guild.getAlliance();
        Char other;
        Guild otherGuild;
        World world = chr.getClient().getWorld();
        GuildMember member = guild == null ? null : chr.getGuild().getMemberByCharID(chr.getId());
        GuildMember otherMember;
        if (!chr.isGuildMaster()) {
            return;
        }
        switch (at) {
            case Req_Withdraw:
                if (member.getAllianceGrade() == 1) {
                    if (alliance.getGuilds().size() <= 1) {
                        alliance.broadcast(WvsContext.allianceResult(AllianceResult.withdraw(alliance, guild, false)));
                        alliance.removeGuild(guild);
                        DatabaseManager.deleteFromDB(alliance);
                    } else {
                        Set<Guild> remainingGuilds = new HashSet<>(alliance.getGuilds());
                        remainingGuilds.remove(guild);
                        Guild newLeadingGuild = Util.getRandomFromCollection(remainingGuilds);
                        otherMember = newLeadingGuild.getGuildLeader();
                        otherMember.setAllianceGrade(1);
                        alliance.broadcast(WvsContext.allianceResult(AllianceResult.changeMaster(alliance, member, otherMember)));
                        alliance.broadcast(WvsContext.allianceResult(AllianceResult.withdraw(alliance, guild, false)));
                        alliance.removeGuild(guild);
                    }
                } else {
                    alliance.broadcast(WvsContext.allianceResult(AllianceResult.withdraw(alliance, guild, false)));
                    alliance.removeGuild(guild);
                }
                break;
            case Req_Invite:
                String guildName = inPacket.decodeString();
                otherGuild = world.getGuildByName(guildName);
                if (otherGuild != null) {
                    other = world.getCharByID(otherGuild.getLeaderID());
                    if (other != null) {
                        if (other.getGuild().getAlliance() == null) {
                            other.write(WvsContext.allianceResult(AllianceResult.inviteGuild(alliance, member)));
                        } else {
                            other.write(WvsContext.allianceResult(AllianceResult.msg(AllianceType.Res_InviteGuild_AlreadyInvited)));
                        }
                    } else {
                        chr.write(WvsContext.allianceResult(AllianceResult.msg(AllianceType.Res_Invite_Failed)));
                    }
                } else {
                    chr.write(WvsContext.allianceResult(AllianceResult.msg(AllianceType.Res_Invite_Failed)));
                }
                break;
            case Req_Load:
                chr.write(WvsContext.allianceResult(AllianceResult.loadDone(alliance)));
                chr.write(WvsContext.allianceResult(AllianceResult.loadGuildDone(alliance)));
                break;
            case Req_ChangeMaster:
                other = world.getCharByID(inPacket.decodeInt());
                if (other != null) {
                    otherMember = other.getGuild().getMemberByCharID(other.getId());
                    member.setAllianceGrade(2);
                    otherMember.setAllianceGrade(1);
                    alliance.broadcast(WvsContext.allianceResult(AllianceResult.changeMaster(alliance, member, otherMember)));
                } else {
                    chr.chatMessage("That character is not online.");
                }
                break;
            case Req_Kick:
                int otherID = inPacket.decodeInt();
                int kickedGuildID = inPacket.decodeInt();
                otherGuild = alliance.getGuildByID(kickedGuildID);
                if (otherGuild != null) {
                    alliance.broadcast(WvsContext.allianceResult(AllianceResult.withdraw(alliance, otherGuild, true)));
                    alliance.removeGuild(otherGuild);
                }
                break;
            case Req_SetGradeName:
                for (int i = 0; i < 5; i++) {
                    String gradeName = inPacket.decodeString();
                    if (gradeName.length() >= 4 && gradeName.length() <= 10) {
                        alliance.getGradeNames().set(i, gradeName);
                    }
                }
                alliance.broadcast(WvsContext.allianceResult(AllianceResult.setGradeName(alliance)));
                break;
            default:
                log.error(String.format("Unhandled alliance request type %s", at.getVal()));
        }
    }

    public static void handleGuildBbs(Char chr, InPacket inPacket) {
        byte idk1 = inPacket.decodeByte();
        byte idk2 = inPacket.decodeByte();
        byte idk3 = inPacket.decodeByte();
        String subject = inPacket.decodeString();
        String content = inPacket.decodeString();
        int icon = inPacket.decodeInt();
    }

    public static void handleUserCreateAuraByGrenade(Client c, InPacket inPacket) {
        int objID = inPacket.decodeInt();
        int skillID = inPacket.decodeInt();
        Position position = inPacket.decodePosition();
        byte isLeft = inPacket.decodeByte();
        Char chr = c.getChr();
        SkillInfo fci = SkillData.getSkillInfoById(skillID);
        byte slv = (byte) chr.getSkill(SkillConstants.getActualSkillIDfromSkillID(skillID)).getCurrentLevel();
        AffectedArea aa = AffectedArea.getPassiveAA(chr, skillID, slv);
        aa.setMobOrigin((byte) 0);
        aa.setPosition(position);
        aa.setSkillID(skillID);
        aa.setSlv(slv);
        aa.setRect(aa.getPosition().getRectAround(fci.getRects().get(0)));
        chr.getField().spawnAffectedArea(aa);
    }

    public static void handleUserSkillUseRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        inPacket.decodeInt(); // crc
        int skillID = inPacket.decodeInt();
        byte slv = inPacket.decodeByte();
        if (chr.checkAndSetSkillCooltime(skillID) || chr.hasSkillCDBypass()) {
            chr.getField().broadcastPacket(UserRemote.effect(chr.getId(), Effect.skillUse(skillID, slv, 0)));
            log.debug("SkillID: " + skillID);
            c.getChr().chatMessage(ChatType.Mob, "SkillID: " + skillID);
            Job sourceJobHandler = c.getChr().getJobHandler();
            SkillInfo si = SkillData.getSkillInfoById(skillID);
            if (si.isMassSpell() && sourceJobHandler.isBuff(skillID) && chr.getParty() != null) {
                Rect r = si.getFirstRect();
                if (r != null) {
                    Rect rectAround = chr.getRectAround(r);
                    for (PartyMember pm : chr.getParty().getOnlineMembers()) {
                        if (pm.getChr() != null
                                && pm.getFieldID() == chr.getFieldID()
                                && rectAround.hasPositionInside(pm.getChr().getPosition())) {
                            Char ptChr = pm.getChr();
                            Effect effect = Effect.skillAffected(skillID, slv, 0);
                            if (ptChr != chr) { // Caster shouldn't get the Affected Skill Effect
                                chr.getField().broadcastPacket(
                                        UserRemote.effect(ptChr.getId(), effect)
                                        , ptChr);
                                ptChr.write(User.effect(effect));
                            }
                            sourceJobHandler.handleSkill(pm.getChr().getClient(), skillID, slv, inPacket);
                        }
                    }
                }
            } else {
                sourceJobHandler.handleSkill(c, skillID, slv, inPacket);
            }
        }
        WvsContext.dispose(c.getChr());
    }

    public static void handleUserShopRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        byte type = inPacket.decodeByte();
        ShopRequestType shr = ShopRequestType.getByVal(type);
        if (shr == null) {
            log.error(String.format("Unhandled shop request type %d", type));
        }
        NpcShopDlg nsd = chr.getShop();
        if (nsd == null) {
            chr.chatMessage("You are currently not in a shop.");
            return;
        }
        switch (shr) {
            case BUY:
                short itemIndex = inPacket.decodeShort();
                int itemID = inPacket.decodeInt();
                short quantity = inPacket.decodeShort();
                NpcShopItem nsi = nsd.getItemByIndex(itemIndex);
                if (nsi == null || nsi.getItemID() != itemID) {
                    chr.chatMessage("The server's item at that position was different than the client's.");
                    log.warn(String.format("Possible hack: expected shop itemID %d, got %d (chr %d)", nsi.getItemID(), itemID, chr.getId()));
                    return;
                }
                if (!chr.canHold(itemID)) {
                    chr.write(ShopDlg.shopResult(new MsgShopResult(ShopResultType.FullInvMsg)));
                    return;
                }
                if (nsi.getTokenItemID() != 0) {
                    int cost = nsi.getTokenPrice() * quantity;
                    if (chr.hasItemCount(nsi.getTokenItemID(), cost)) {
                        chr.consumeItem(nsi.getTokenItemID(), cost);
                    } else {
                        chr.write(ShopDlg.shopResult(new MsgShopResult(ShopResultType.NotEnoughMesosMsg)));
                        return;
                    }
                } else {
                    long cost = nsi.getPrice() * quantity;
                    if (chr.getMoney() < cost) {
                        chr.write(ShopDlg.shopResult(new MsgShopResult(ShopResultType.NotEnoughMesosMsg)));
                        return;
                    }
                    chr.deductMoney(cost);
                }
                int itemQuantity = nsi.getQuantity() > 0 ? nsi.getQuantity() : 1;
                Item item = ItemData.getItemDeepCopy(itemID);
                item.setQuantity(quantity * itemQuantity);
                chr.addItemToInventory(item);
                chr.write(ShopDlg.shopResult(new MsgShopResult(ShopResultType.Success)));
                break;
            case RECHARGE:
                short slot = inPacket.decodeShort();
                item = chr.getConsumeInventory().getItemBySlot(slot);
                if (item == null || !ItemConstants.isRechargable(item.getItemId())) {
                    chr.chatMessage(String.format("Was not able to find a rechargable item at position %d.", slot));
                    return;
                }
                ItemInfo ii = ItemData.getItemInfoByID(item.getItemId());
                long cost = ii.getSlotMax() - item.getQuantity();
                if (chr.getMoney() < cost) {
                    chr.write(ShopDlg.shopResult(new MsgShopResult(ShopResultType.NotEnoughMesosMsg)));
                    return;
                }
                chr.deductMoney(cost);
                item.addQuantity(ii.getSlotMax());
                chr.write(WvsContext.inventoryOperation(true, false,
                        InventoryOperation.UPDATE_QUANTITY, slot, (short) 0, 0, item));
                chr.write(ShopDlg.shopResult(new MsgShopResult(ShopResultType.Success)));
                break;
            case SELL:
                slot = inPacket.decodeShort();
                itemID = inPacket.decodeInt();
                quantity = inPacket.decodeShort();
                InvType it = ItemConstants.getInvTypeByItemID(itemID);
                item = chr.getInventoryByType(it).getItemBySlot(slot);
                if (item == null || item.getItemId() != itemID) {
                    chr.chatMessage("Could not find that item.");
                    return;
                }
                if (ItemConstants.isEquip(itemID)) {
                    cost = ((Equip) item).getPrice();
                } else {
                    cost = ItemData.getItemInfoByID(itemID).getPrice() * quantity;
                }
                chr.consumeItem(itemID, quantity);
                chr.addMoney(cost);
                chr.write(ShopDlg.shopResult(new MsgShopResult(ShopResultType.Success)));
                break;
            case CLOSE:
                chr.setShop(null);
                break;
            default:
                log.error(String.format("Unhandled shop request type %s", shr));
        }
        chr.dispose();
    }

    public static void handleLoadAccountIDOfCharacterFriendRequest(Client c, InPacket inPacket) {
        c.write(WvsContext.loadAccountIDOfCharacterFriendResult(c.getChr().getFriends()));
    }

    public static void handleFriendRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        byte type = inPacket.decodeByte();
        FriendType ft = Arrays.stream(FriendType.values()).filter(f -> f.getVal() == type).findFirst().orElse(null);
        if (ft == null) {
            chr.chatMessage("Unknown friend request type.");
            log.error(String.format("Unknown friend request type %d", type));
            return;
        }
        Char other;
        switch (ft) {
            case FriendReq_SetFriend:
                String name = inPacket.decodeString();
                other = c.getWorld().getCharByName(name);
                if (other == null) {
                    c.write(WvsContext.friendResult(new FriendResultMsg(FriendType.FriendRes_SetFriend_UnknownUser)));
                    return;
                }
                String groupName = inPacket.decodeString();
                String memo = inPacket.decodeString();
                boolean account = inPacket.decodeByte() != 0;
                String nick = "";
                if (account) {
                    nick = inPacket.decodeString();
                    if (nick.equalsIgnoreCase("")) {
                        nick = name;
                    }
                }
                Friend friend = new Friend();
                friend.setFriendID(other.getId());
                friend.setGroup(groupName);
                friend.setMemo(memo);
                friend.setName(name);
                friend.setFriendAccountID(other.getAccId());
                if (account) {
                    friend.setNickname(nick);
                    friend.setFlag(FriendFlag.AccountFriendOffline);
                    chr.getAccount().addFriend(friend);
                } else {
                    chr.getFriends().add(friend);
                    friend.setFlag(FriendFlag.FriendOffline);
                }
                Friend otherFriend = new Friend();
                otherFriend.setFriendID(chr.getId());
                otherFriend.setName(chr.getName());
                otherFriend.setFriendAccountID(chr.getAccId());
                otherFriend.setGroup("Default Group");
                if (account) {
                    otherFriend.setNickname(chr.getName());
                    otherFriend.setFlag(FriendFlag.AccountFriendRequest);
                    other.getAccount().addFriend(otherFriend);
                } else {
                    otherFriend.setFlag(FriendFlag.FriendRequest);
                    other.addFriend(otherFriend);
                }
                c.write(WvsContext.friendResult(new FriendResultMsg(FriendType.FriendRes_SetFriend_Done, name)));
                c.write(WvsContext.friendResult(new LoadFriendResult(chr.getAllFriends())));
                other.write(WvsContext.friendResult(
                        new InviteFriendResult(otherFriend, account, chr.getLevel(), chr.getJob(), chr.getSubJob())));
                break;
            case FriendReq_AcceptFriend:
                int friendID = inPacket.decodeInt();
                boolean online = true;
                Char requestor = c.getWorld().getCharByID(friendID);
                if (requestor == null) {
                    requestor = Char.getFromDBById(friendID);
                    online = false;
                    if (requestor == null) {
                        c.write(WvsContext.friendResult(new FriendResultMsg(FriendType.FriendRes_SetFriend_UnknownUser)));
                        return;
                    }
                }
                friend = chr.getFriendByCharID(friendID);
                friend.setFlag(online ? FriendFlag.AccountFriendOnline : FriendFlag.AccountFriendOffline);
                if (online) {
                    friend.setChannelID(requestor.getClient().getChannel());
                    otherFriend = requestor.getFriendByCharID(chr.getId());
                    otherFriend.setChannelID(c.getChannel());
                    otherFriend.setFlag(FriendFlag.AccountFriendOnline);
                    requestor.write(WvsContext.friendResult(new UpdateFriendResult(otherFriend)));
                    requestor.chatMessage(String.format("%s has accepted your friend request!", chr.getName()));
                }
                c.write(WvsContext.friendResult(new UpdateFriendResult(friend)));
                if (!online) {
                    DatabaseManager.saveToDB(requestor);
                }
                break;
            case FriendReq_AcceptAccountFriend:
                int accountID = inPacket.decodeInt();
                Account accountRef = c.getWorld().getAccountByID(accountID);
                Account myAcc = chr.getAccount();
                online = true;
                if (accountRef == null) {
                    online = false;
                    accountRef = Account.getFromDBById(accountID);
                    if (accountRef == null) {
                        chr.write(WvsContext.friendResult(new FriendResultMsg(FriendType.FriendRes_SetFriend_UnknownUser)));
                        return;
                    }
                }
                friend = myAcc.getFriendByAccID(accountID);
                friend.setFlag(online ? FriendFlag.AccountFriendOnline : FriendFlag.AccountFriendOffline);
                otherFriend = accountRef.getFriendByAccID(myAcc.getId());
                otherFriend.setFlag(FriendFlag.AccountFriendOnline);
                otherFriend.setChannelID(chr.getClient().getChannel());
                if (online) {
                    requestor = accountRef.getCurrentChr();
                    friend.setChannelID(requestor.getClient().getChannel());
                    requestor.chatMessage(String.format("%s has accepted your account friend request!", chr.getName()));
                    requestor.write(WvsContext.friendResult(new UpdateFriendResult(otherFriend)));
                } else {
                    DatabaseManager.saveToDB(otherFriend);
                }
                c.write(WvsContext.friendResult(new UpdateFriendResult(friend)));
                break;
            case FriendReq_DeleteFriend:
                friendID = inPacket.decodeInt();
                friend = chr.getFriendByCharID(friendID);
                if (friend == null) {
                    chr.write(WvsContext.friendResult(new FriendResultMsg(FriendType.FriendRes_SetFriend_UnknownUser)));
                    return;
                }
                other = c.getWorld().getCharByID(friendID);
                online = true;
                if (other == null) {
                    online = false;
                    other = Char.getFromDBById(friendID);
                }
                otherFriend = other == null ? null : other.getFriendByCharID(chr.getId());
                if (other != null) {
                    other.removeFriend(otherFriend);
                }
                if (online) {
                    other.write(WvsContext.friendResult(new RemoveFriendResult(otherFriend)));
                }
                other.removeFriendByID(chr.getId());
                chr.removeFriend(friend);
                chr.write(WvsContext.friendResult(new RemoveFriendResult(friend)));
                DatabaseManager.saveToDB(other);
                DatabaseManager.saveToDB(chr);
                break;
            case FriendReq_DeleteAccountFriend:
                int accID = inPacket.decodeInt();
                accountRef = chr.getAccount();
                friend = accountRef.getFriendByAccID(accID);
                if (friend == null) {
                    chr.write(WvsContext.friendResult(new FriendResultMsg(FriendType.FriendRes_SetFriend_UnknownUser)));
                    return;
                }
                online = true;
                Account otherAccount = c.getWorld().getAccountByID(accID);
                otherFriend = otherAccount.getFriendByAccID(chr.getAccId());
                if (otherAccount == null) {
                    otherAccount = Account.getFromDBById(accID);
                    online = false;
                }
                if (otherAccount != null) {
                    otherAccount.removeFriend(otherFriend);
                }
                if (online) {
                    otherAccount.getCurrentChr().write(WvsContext.friendResult(new RemoveFriendResult(otherFriend)));
                }
                accountRef.removeFriend(friend);
                DatabaseManager.saveToDB(accountRef);
                DatabaseManager.saveToDB(otherAccount);
                chr.write(WvsContext.friendResult(new RemoveFriendResult(friend)));
                break;
            case FriendReq_RefuseFriend:
                friendID = inPacket.decodeInt();
                friend = chr.getFriendByCharID(friendID);
                if (friend == null) {
                    chr.write(WvsContext.friendResult(new FriendResultMsg(FriendType.FriendRes_SetFriend_UnknownUser)));
                    return;
                }
                chr.write(WvsContext.friendResult(new RemoveFriendResult(friend)));
                chr.removeFriend(friend);
                other = c.getWorld().getCharByID(friendID);
                if (other == null) {
                    other = Char.getFromDBById(friendID);
                    if (other == null) {
                        return;
                    }
                    other.removeFriendByID(chr.getId());
                    DatabaseManager.saveToDB(other);
                } else {
                    other.write(WvsContext.friendResult(new RemoveFriendResult(other.getFriendByCharID(chr.getId()))));
                    other.removeFriendByID(chr.getId());
                }
                break;
            case FriendReq_RefuseAccountFriend:
                accID = inPacket.decodeInt();
                accountRef = chr.getAccount();
                friend = accountRef.getFriendByAccID(accID);
                if (friend == null) {
                    chr.write(WvsContext.friendResult(new FriendResultMsg(FriendType.FriendRes_SetFriend_UnknownUser)));
                    return;
                }
                chr.write(WvsContext.friendResult(new RemoveFriendResult(friend)));
                accountRef.removeFriend(friend);
                otherAccount = c.getWorld().getAccountByID(accID);
                if (otherAccount == null) {
                    otherAccount = Account.getFromDBById(accID);
                    if (otherAccount == null) {
                        return;
                    }
                    otherAccount.removeFriend(accID);
                    DatabaseManager.saveToDB(otherAccount);
                } else {
                    other = accountRef.getCurrentChr();
                    other.write(WvsContext.friendResult(new RemoveFriendResult(other.getFriendByCharID(chr.getId()))));
                    otherAccount.removeFriend(chr.getId());
                }
                break;
            case FriendReq_ModifyAccountFriendGroup:
                accID = inPacket.decodeInt();
                friend = chr.getAccount().getFriendByAccID(accID);
                if (friend != null) {
                    friend.setGroup(inPacket.decodeString());
                    chr.write(WvsContext.friendResult(new UpdateFriendResult(friend)));
                }
                break;
            case FriendReq_ModifyFriend:
                account = inPacket.decodeByte() != 0;
                friendID = inPacket.decodeInt();
                accID = inPacket.decodeInt();
                friend = account ? chr.getAccount().getFriendByAccID(accID) : chr.getFriendByCharID(friendID);
                friend.setNickname(inPacket.decodeString());
                friend.setMemo(inPacket.decodeString());
                chr.write(WvsContext.friendResult(new UpdateFriendResult(friend)));
                break;
            default:
                chr.chatMessage(String.format("Unhandled friend request type %s", ft.toString()));
                log.error(String.format("Unhandled friend request type %s", ft.toString()));
                break;
        }
    }

    public static void handleUserMacroSysDataModified(Client c, InPacket inPacket) {
        byte size = inPacket.decodeByte();
        List<Macro> macros = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Macro macro = new Macro();
            macro.setName(inPacket.decodeString());
            macro.setMuted(inPacket.decodeByte() != 0);
            for (int j = 0; j < 3; j++) {
                macro.setSkillAtPos(j, inPacket.decodeInt());
            }
            macros.add(macro);
        }
        c.getChr().getMacros().clear();
        c.getChr().getMacros().addAll(macros); // don't set macros directly, as a new row will be made in the DB
    }

    public static void handleUserCreateHolidomRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        Field field = chr.getField();

        inPacket.decodeInt(); //tick
        inPacket.decodeByte(); //unk
        int skillID = inPacket.decodeInt();
        inPacket.decodeInt(); //unk

        if (field.getAffectedAreas().stream().noneMatch(ss -> ss.getSkillID() == skillID)) {
            log.error(String.format("Character %d tried to heal from Holy Fountain (%d) whilst there isn't any on the field.", chr.getId(), skillID));
            return;
        }
        c.getChr().heal((int) (c.getChr().getMaxHP() / ((double) 100 / 40)));
    }

    public static void handleSummonedSkill(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        Field field = chr.getField();

        int objectID = inPacket.decodeInt();
        int skillId = inPacket.decodeInt();
        //5 more bytes, unknown

        if (field.getLifeByObjectID(objectID) != null && field.getLifeByObjectID(objectID) instanceof Summon) {
            Summon summon = (Summon) field.getLifeByObjectID(objectID);
            summon.onSkillUse(skillId);
        }
    }

    public static void handleUserTrunkRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        Trunk trunk = chr.getAccount().getTrunk();
        byte req = inPacket.decodeByte();
        TrunkType trunkType = TrunkType.getByVal(req);
        if (trunkType == null) {
            log.error(String.format("Unknown trunk request type %d.", req));
            return;
        }
        switch (trunkType) {
            case TrunkReq_Money:
                long reqMoney = inPacket.decodeLong();
                boolean put = reqMoney < 0;
                long curMoney = chr.getMoney();
                if (put) {
                    reqMoney = -reqMoney;
                    if (reqMoney > curMoney && trunk.canAddMoney(reqMoney)) {
                        chr.write(CField.trunkDlg(new TrunkMsg(TrunkType.TrunkRes_PutNoMoney)));
                        return;
                    }
                    trunk.addMoney(reqMoney);
                    chr.deductMoney(reqMoney);
                    chr.write(CField.trunkDlg(new TrunkUpdate(TrunkType.TrunkRes_MoneySuccess, trunk)));
                } else {
                    if (reqMoney <= trunk.getMoney() && chr.canAddMoney(reqMoney)) {
                        trunk.addMoney(-reqMoney);
                        chr.addMoney(reqMoney);
                        chr.write(CField.trunkDlg(new TrunkUpdate(TrunkType.TrunkRes_MoneySuccess, trunk)));
                    } else {
                        chr.write(CField.trunkDlg(new TrunkMsg(TrunkType.TrunkRes_GetNoMoney)));
                    }
                }
                break;
            case TrunkReq_GetItem:
                short pos = (short) (inPacket.decodeShort() - 1);
                if (pos >= 0 && pos < trunk.getItems().size()) {
                    Item getItem = trunk.getItems().get(pos);
                    if (chr.getInventoryByType(getItem.getInvType()).canPickUp(getItem)) {
                        trunk.removeItem(getItem);
                        chr.addItemToInventory(getItem);
                        chr.write(CField.trunkDlg(new TrunkUpdate(TrunkType.TrunkRes_GetSuccess, trunk)));
                    } else {
                        chr.write(CField.trunkDlg(new TrunkMsg(TrunkType.TrunkRes_PutNoSpace)));
                    }
                } else {
                    chr.write(CField.trunkDlg(new TrunkMsg(TrunkType.TrunkRes_GetUnknown)));
                }
                break;
            case TrunkReq_PutItem:
                short slot = inPacket.decodeShort();
                int itemID = inPacket.decodeInt();
                short quantity = inPacket.decodeShort();
                InvType invType = ItemConstants.getInvTypeByItemID(itemID);
                Item item = chr.getInventoryByType(invType).getItemBySlot(slot);
                if (item != null && quantity > 0 && item.getQuantity() >= quantity && item.getItemId() == itemID) {
                    chr.consumeItem(itemID, quantity);
                    trunk.addItem(item, quantity);
                    chr.write(CField.trunkDlg(new TrunkUpdate(TrunkType.TrunkRes_PutSuccess, trunk)));
                } else {
                    chr.write(CField.trunkDlg(new TrunkMsg(TrunkType.TrunkRes_PutUnknown)));
                }
                break;
            case TrunkReq_SortItem:
//                trunk.getItems().sort(Comparator.comparingInt(Item::getItemId));
                chr.write(CField.trunkDlg(new TrunkUpdate(TrunkType.TrunkRes_SortItem, trunk)));
                break;
            case TrunkReq_CloseDialog:
                chr.dispose();
                break;
            default:
                log.error(String.format("Unhandled trunk request type %s.", trunkType));
        }
    }

    public static void handleMobExplosionStart(Client c, InPacket inPacket) {
        int mobID = inPacket.decodeInt();
        int charID = inPacket.decodeInt();
        int skillID = inPacket.decodeInt(); //tick
        Char chr = c.getChr();
        if (JobConstants.isXenon(chr.getJob())) {
            skillID = Xenon.TRIANGULATION;
        }
        if (JobConstants.isDawnWarrior(chr.getJob())) {
            skillID = 11121013;
        }
        if (JobConstants.isAngelicBuster(chr.getId())) {
            skillID = 65101006; //Lovely Sting Explosion
        }
        Mob mob = (Mob) c.getChr().getField().getLifeByObjectID(mobID);
        if (mob != null) {
            MobTemporaryStat mts = mob.getTemporaryStat();
            if ((mts.hasCurrentMobStat(MobStat.Explosion) && mts.getCurrentOptionsByMobStat(MobStat.Explosion).wOption == chr.getId()) ||
                    (mts.hasCurrentMobStat(MobStat.SoulExplosion) && mts.getCurrentOptionsByMobStat(MobStat.SoulExplosion).wOption == chr.getId())) {
                c.write(UserLocal.explosionAttack(skillID, mob.getPosition(), mobID, 1));
                mts.removeMobStat(MobStat.Explosion, true);
                mts.removeMobStat(MobStat.SoulExplosion, true);
            }
        }
    }

    public static void handleUserActivateEffectItem(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        chr.setActiveEffectItemID(inPacket.decodeInt());
    }

    public static void handleUserActivatePetRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        inPacket.decodeInt(); // tick
        short slot = inPacket.decodeShort();
        Item item = chr.getCashInventory().getItemBySlot(slot);
        if (!(item instanceof PetItem)) {
            item = chr.getConsumeInventory().getItemBySlot(slot);
        }
        // Two of the same condition, as item had to be re-assigned
        if (!(item instanceof PetItem)) {
            chr.chatMessage(String.format("Could not find a pet on that slot (slot %s).", slot));
        }
        PetItem petItem = (PetItem) item;
        if (petItem.getActiveState() == 0) {
            if (chr.getPets().size() >= GameConstants.MAX_PET_AMOUNT) {
                return;
            }
            Pet pet = petItem.createPet(chr);
            petItem.setActiveState((byte) (pet.getIdx() + 1));
            chr.addPet(pet);
            c.write(UserLocal.petActivateChange(pet, true, (byte) 0));
        } else {
            Pet pet = chr.getPets()
                    .stream()
                    .filter(p -> p.getItem().getActiveState() == petItem.getActiveState())
                    .findFirst().orElse(null);
            petItem.setActiveState((byte) 0);
            chr.removePet(pet);
            c.write(UserLocal.petActivateChange(pet, false, (byte) 0));
        }

        c.write(WvsContext.inventoryOperation(true, false,
                InventoryOperation.ADD, (short) petItem.getBagIndex(), (short) 0, 0, petItem));
        chr.dispose();
    }

    public static void handleUserPetFoodItemUseRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        inPacket.decodeInt(); // tick
        short slot = inPacket.decodeShort();
        int itemID = inPacket.decodeInt();
        // TODO check properly for items here
        Item item = chr.getConsumeInventory().getItemBySlot(slot);
        if (item != null) {
            chr.consumeItem(item);
            for (Pet pet : chr.getPets()) {
                PetItem pi = pet.getItem();
                // max repleteness is 100
                pi.setRepleteness((byte) (Math.min(100, pi.getRepleteness() + 30)));
                c.write(WvsContext.inventoryOperation(true, false,
                        InventoryOperation.ADD, (short) pi.getBagIndex(), (short) 0, 0, pi));
            }
        }
    }

    public static void handleReactorClick(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        int objID = inPacket.decodeInt();
        int idk = inPacket.decodeInt();
        byte type = inPacket.decodeByte();
        Life life = chr.getField().getLifeByObjectID(objID);
        if (!(life instanceof Reactor)) {
            log.error("Could not find reactor with objID " + objID);
            return;
        }
        Reactor reactor = (Reactor) life;
        int templateID = reactor.getTemplateId();
        ReactorInfo ri = ReactorData.getReactorInfoByID(templateID);
        String action = ri.getAction();
        if (chr.getScriptManager().isActive(ScriptType.Reactor)
                && chr.getScriptManager().getParentIDByScriptType(ScriptType.Reactor) == templateID) {
            try {
                chr.getScriptManager().getInvocableByType(ScriptType.Reactor).invokeFunction("action", reactor, type);
            } catch (ScriptException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        } else {
            chr.getScriptManager().startScript(templateID, objID, action, ScriptType.Reactor);
        }
    }

    public static void handleReactorRectInMob(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        int objID = inPacket.decodeInt();

        Life life = chr.getField().getLifeByObjectID(objID);
        if (!(life instanceof Reactor)) {
            log.error("Could not find reactor with objID " + objID);
            return;
        }
        Reactor reactor = (Reactor) life;
        int templateID = reactor.getTemplateId();
        ReactorInfo ri = ReactorData.getReactorInfoByID(templateID);
        String action = ri.getAction();
        if (action.equals("")) {
            action = templateID + "action";
        }
        if (chr.getScriptManager().isActive(ScriptType.Reactor)
                && chr.getScriptManager().getParentIDByScriptType(ScriptType.Reactor) == templateID) {
            try {
                chr.getScriptManager().getInvocableByType(ScriptType.Reactor).invokeFunction("action", 0);
            } catch (ScriptException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        } else {
            chr.getScriptManager().startScript(templateID, objID, action, ScriptType.Reactor);
        }
    }

    public static void handleUserEquipmentEnchantWithSingleUIRequest(Client c, InPacket inPacket) {
        byte equipmentEnchantType = inPacket.decodeByte();

        Char chr = c.getChr();
        EquipmentEnchantType eeType = EquipmentEnchantType.getByVal(equipmentEnchantType);

        if (eeType == null) {
            log.error(String.format("Unknown enchant UI request %d", equipmentEnchantType));
            chr.write(CField.showUnknownEnchantFailResult((byte) 0));
            return;
        }

        switch (eeType) {
            case ScrollUpgradeRequest:
                inPacket.decodeInt();// tick
                short pos = inPacket.decodeShort();
                int scrollID = inPacket.decodeInt();
                Inventory inv = pos < 0 ? chr.getEquippedInventory() : chr.getEquipInventory();
                pos = (short) Math.abs(pos);
                Equip equip = (Equip) inv.getItemBySlot(pos);
                Equip prevEquip = equip.deepCopy();
                if (equip == null || equip.getBaseStat(tuc) <= 0 || equip.hasSpecialAttribute(EquipSpecialAttribute.Vestige)) {
                    log.error(String.format("Character %d tried to enchant a non-scrollable equip (pos %d, itemid %d).",
                            chr.getId(), pos, equip == null ? 0 : equip.getItemId()));
                    chr.write(CField.showUnknownEnchantFailResult((byte) 0));
                    return;
                }
                List<ScrollUpgradeInfo> suis = ItemConstants.getScrollUpgradeInfosByEquip(equip);
                if (scrollID < 0 || scrollID >= suis.size()) {
                    log.error(String.format("Characer %d tried to spell trace scroll with an invalid scoll ID (%d, " +
                            "itemID %d).", chr.getId(), scrollID, equip.getItemId()));
                    chr.write(CField.showUnknownEnchantFailResult((byte) 0));
                    return;
                }
                ScrollUpgradeInfo sui = suis.get(scrollID);
                chr.consumeItem(ItemConstants.SPELL_TRACE_ID, sui.getCost());
                boolean success = sui.applyTo(equip);
                equip.recalcEnchantmentStats();
                String desc = success ? "Your item has been upgraded." : "Your upgrade has failed.";
                chr.write(CField.showScrollUpgradeResult(false, success ? 1 : 0, desc, prevEquip, equip));
                equip.updateToChar(chr);
                if (equip.getBaseStat(tuc) > 0) {
                    suis = ItemConstants.getScrollUpgradeInfosByEquip(equip);
                    c.write(CField.scrollUpgradeDisplay(false, suis));
                }
                break;
            case HyperUpgradeResult:
                inPacket.decodeInt(); //tick
                int eqpPos = inPacket.decodeShort();
                boolean extraChanceFromMiniGame = inPacket.decodeByte() != 0;
                equip = (Equip) chr.getEquipInventory().getItemBySlot((short) eqpPos);
                boolean equippedInv = eqpPos < 0;
                inv = equippedInv ? chr.getEquippedInventory() : chr.getEquipInventory();
                equip = (Equip) inv.getItemBySlot((short) Math.abs(eqpPos));
                if (equip == null) {
                    chr.chatMessage("Could not find the given equip.");
                    chr.write(CField.showUnknownEnchantFailResult((byte) 0));
                    return;
                }
                if (!ItemConstants.isUpgradable(equip.getItemId()) ||
                        (equip.getBaseStat(tuc) != 0 && !c.getWorld().isReboot()) ||
                        chr.getEquipInventory().getEmptySlots() == 0 ||
                        equip.getChuc() >= GameConstants.getMaxStars(equip) ||
                        equip.hasSpecialAttribute(EquipSpecialAttribute.Vestige)) {
                    chr.chatMessage("Equipment cannot be enhanced.");
                    chr.write(CField.showUnknownEnchantFailResult((byte) 0));
                    return;
                }
                long cost = GameConstants.getEnchantmentMesoCost(equip.getrLevel() + equip.getiIncReq(), equip.getChuc(), equip.isSuperiorEqp());
                if (chr.getMoney() < cost) {
                    chr.chatMessage("Mesos required: " + NumberFormat.getNumberInstance(Locale.US).format(cost));
                    chr.write(CField.showUnknownEnchantFailResult((byte) 0));
                    return;
                }
                Equip oldEquip = equip.deepCopy();
                int successProp = GameConstants.getEnchantmentSuccessRate(equip);
                if (extraChanceFromMiniGame) {
                    successProp *= 1.045;
                }
                int destroyProp = GameConstants.getEnchantmentDestroyRate(equip);
                if (equippedInv && destroyProp > 0 && chr.getEquipInventory().getEmptySlots() == 0) {
                    c.write(WvsContext.broadcastMsg(BroadcastMsg.popUpMessage("You do not have enough space in your " +
                            "equip inventory in case your item gets destroyed.")));
                    return;
                }
                success = Util.succeedProp(successProp, 1000);
                boolean boom = false;
                boolean canDegrade = equip.isSuperiorEqp() ? equip.getChuc() > 0 : equip.getChuc() > 5 && equip.getChuc() % 5 != 0;
                if (success) {
                    equip.setChuc((short) (equip.getChuc() + 1));
                    equip.setDropStreak(0);
                } else if (Util.succeedProp(destroyProp, 1000)) {
                    equip.setChuc((short) 0);
                    equip.addSpecialAttribute(EquipSpecialAttribute.Vestige);
                    boom = true;
                    if (equippedInv) {
                        chr.unequip(equip);
                        equip.setBagIndex(chr.getEquipInventory().getFirstOpenSlot());
                        equip.updateToChar(chr);
                        c.write(WvsContext.inventoryOperation(true, false, MOVE, (short) eqpPos, (short) equip.getBagIndex(), 0, equip));
                        if (!equip.isSuperiorEqp()) {
                            equip.setChuc((short) Math.min(12, equip.getChuc()));
                        } else {
                            equip.setChuc((short) 0);
                        }
                    }
                } else if (canDegrade) {
                    equip.setChuc((short) (equip.getChuc() - 1));
                    equip.setDropStreak(equip.getDropStreak() + 1);
                }
                chr.deductMoney(cost);
                equip.recalcEnchantmentStats();
                oldEquip.recalcEnchantmentStats();
                equip.updateToChar(chr);
                c.write(CField.showUpgradeResult(oldEquip, equip, success, boom, canDegrade));
                chr.dispose();
                break;
            case TransmissionResult:
                inPacket.decodeInt(); // tick
                short toPos = inPacket.decodeShort();
                short fromPos = inPacket.decodeShort();
                Equip fromEq = (Equip) chr.getEquipInventory().getItemBySlot(fromPos);
                Equip toEq = (Equip) chr.getEquipInventory().getItemBySlot(toPos);
                if (fromEq == null || toEq == null || fromEq.getItemId() != toEq.getItemId() ||
                        !fromEq.hasSpecialAttribute(EquipSpecialAttribute.Vestige)) {
                    log.error(String.format("Equip transmission failed: from = %s, to = %s", fromEq, toEq));
                    c.write(CField.showUnknownEnchantFailResult((byte) 0));
                    return;
                }
                fromEq.removeSpecialAttribute(EquipSpecialAttribute.Vestige);
                fromEq.setChuc((short) 0);
                chr.consumeItem(toEq);
                fromEq.updateToChar(chr);
                c.write(CField.showTranmissionResult(fromEq, toEq));
                break;
            case ScrollUpgradeDisplay:
                int ePos = inPacket.decodeInt();
                inv = ePos < 0 ? chr.getEquippedInventory() : chr.getEquipInventory();
                ePos = Math.abs(ePos);
                equip = (Equip) inv.getItemBySlot((short) ePos);
                if (c.getWorld().isReboot()) {
                    log.error(String.format("Character %d attempted to scroll in reboot world (pos %d, itemid %d).",
                            chr.getId(), ePos, equip == null ? 0 : equip.getItemId()));
                    chr.dispose();
                    return;
                }
                if (equip == null || equip.getBaseStat(tuc) <= 0 || equip.hasSpecialAttribute(EquipSpecialAttribute.Vestige) || !ItemConstants.isUpgradable(equip.getItemId())) {
                    log.error(String.format("Character %d tried to scroll a non-scrollable equip (pos %d, itemid %d).",
                            chr.getId(), ePos, equip == null ? 0 : equip.getItemId()));
                    chr.dispose();
                    return;
                }
                suis = ItemConstants.getScrollUpgradeInfosByEquip(equip);
                c.write(CField.scrollUpgradeDisplay(false, suis));
                break;
            /*case ScrollTimerEffective:
                break;*/
            case HyperUpgradeDisplay:
                ePos = inPacket.decodeInt();
                inv = ePos < 0 ? chr.getEquippedInventory() : chr.getEquipInventory();
                ePos = Math.abs(ePos);
                equip = (Equip) inv.getItemBySlot((short) ePos);
                if (equip == null || equip.hasSpecialAttribute(EquipSpecialAttribute.Vestige) || !ItemConstants.isUpgradable(equip.getItemId())) {
                    log.error(String.format("Character %d tried to enchant a non-enchantable equip (pos %d, itemid %d).",
                            chr.getId(), ePos, equip == null ? 0 : equip.getItemId()));
                    chr.write(CField.showUnknownEnchantFailResult((byte) 0));
                    return;
                }
                c.write(CField.hyperUpgradeDisplay(equip, equip.getChuc() > 5 && equip.getChuc() % 5 != 0,
                        GameConstants.getEnchantmentMesoCost(equip.getrLevel() + equip.getiIncReq(), equip.getChuc(), equip.isSuperiorEqp()),
                        0, GameConstants.getEnchantmentSuccessRate(equip),
                        GameConstants.getEnchantmentDestroyRate(equip), equip.getDropStreak() >= 2));
                break;
            case MiniGameDisplay:
                c.write(CField.miniGameDisplay(eeType));
                break;
            //case ShowScrollUpgradeResult:
            case ScrollTimerEffective:
            case ShowHyperUpgradeResult:
                break;
            /*
            case ShowScrollVestigeCompensationResult:
            case ShowTransmissionResult:
            case ShowUnknownFailResult:
                break;*/
            default:
                log.debug("Unhandled Equipment Enchant Type: " + eeType);
                chr.write(CField.showUnknownEnchantFailResult((byte) 0));
                break;
        }
    }

    public static void handleUserLearnItemUseRequest(Client c, InPacket inPacket) {
        inPacket.decodeInt(); //tick
        short pos = inPacket.decodeShort();
        int itemID = inPacket.decodeInt();
        Char chr = c.getChr();

        ItemInfo ii = ItemData.getItemInfoByID(itemID);
        int masterLevel = ii.getMasterLv();
        int reqSkillLv = ii.getReqSkillLv();
        int skillid = 0;
        Map<ScrollStat, Integer> vals = ii.getScrollStats();
        int chance = vals.getOrDefault(ScrollStat.success, 100);

        for (int skill : ii.getSkills()) {
            if (chr.hasSkill(skill)) {
                skillid = skill;
                break;
            }
        }
        Skill skill = chr.getSkill(skillid);
        if (skill == null) {
            chr.chatMessage(Notice2, "An error has occured. Mastery Book ID: " + itemID + ",  skill ID: " + skillid + ".");
            chr.dispose();
            return;
        }
        if (skillid == 0 || (skill.getMasterLevel() >= masterLevel) || skill.getCurrentLevel() < reqSkillLv) {
            chr.chatMessage(SystemNotice, "You cannot use this Mastery Book.");
            chr.dispose();
            return;
        }

        if (skill.getCurrentLevel() > reqSkillLv && skill.getMasterLevel() < masterLevel) {
            chr.chatMessage(Mob, "Success Chance: " + chance + "%.");
            if (Util.succeedProp(chance)) {
                skill.setMasterLevel(masterLevel);
                List<Skill> list = new ArrayList<>();
                list.add(skill);
                chr.addSkill(skill);
                chr.getClient().write(WvsContext.changeSkillRecordResult(list, true, false, false, false));
                chr.chatMessage(Notice2, "[Mastery Book] Item id: " + itemID + "  set Skill id: " + skillid + "'s Master Level to: " + masterLevel + ".");
            } else {
                chr.chatMessage(Notice2, "[Mastery Book] Item id: " + itemID + " was used, however it was unsuccessful.");
            }
            chr.consumeItem(itemID, 1);
        }
        chr.dispose();
    }

    public static void handleRequestDecCombo(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        if (JobConstants.isAran(chr.getJob())) {
            Aran aranJobHandler = ((Aran) c.getChr().getJobHandler());
            aranJobHandler.setCombo(aranJobHandler.getCombo() - 10);
        }
    }

    public static void handleUserRequestFlyingSwordStart(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        int maxCount = 3;
        if (Kaiser.getTempBladeSkill(chr, tsm) == Kaiser.TEMPEST_BLADES_FIVE || Kaiser.getTempBladeSkill(chr, tsm) == Kaiser.TEMPEST_BLADES_FIVE_FF) {
            maxCount = 5;
        }
        int mobCount = inPacket.decodeInt();
        int lastMobID = 0;
        int mobid = 0;

        for (int i = 0; i < mobCount; i++) {
            mobid = inPacket.decodeInt();


            Mob mob = (Mob) chr.getField().getLifeByObjectID(mobid);
            int inc = ForceAtomEnum.KAISER_WEAPON_THROW_1.getInc();
            int type = ForceAtomEnum.KAISER_WEAPON_THROW_1.getForceAtomType();

            switch (tsm.getOption(StopForceAtomInfo).nOption) {
                case 3:
                    inc = ForceAtomEnum.KAISER_WEAPON_THROW_MORPH_1.getInc();
                    type = ForceAtomEnum.KAISER_WEAPON_THROW_MORPH_1.getForceAtomType();
                    break;
                case 2:
                    inc = ForceAtomEnum.KAISER_WEAPON_THROW_2.getInc();
                    type = ForceAtomEnum.KAISER_WEAPON_THROW_2.getForceAtomType();
                    break;
                case 4:
                    inc = ForceAtomEnum.KAISER_WEAPON_THROW_MORPH_2.getInc();
                    type = ForceAtomEnum.KAISER_WEAPON_THROW_MORPH_2.getForceAtomType();
                    break;
            }

            ForceAtomInfo forceAtomInfo = new ForceAtomInfo(1, inc, 25, 30,
                    0, 10 * i, (int) System.currentTimeMillis(), 1, 0,
                    new Position());
            chr.getField().broadcastPacket(CField.createForceAtom(false, 0, chr.getId(), type,
                    true, mob.getObjectId(), Kaiser.TEMPEST_BLADES_FIVE_FF, forceAtomInfo, new Rect(), 0, 300,
                    mob.getPosition(), Kaiser.TEMPEST_BLADES_FIVE_FF, mob.getPosition()));

            lastMobID = mobid;
        }


        for (int i = mobCount; i < maxCount; i++) {

            Mob mob = (Mob) chr.getField().getLifeByObjectID(lastMobID);
            int inc = ForceAtomEnum.KAISER_WEAPON_THROW_1.getInc();
            int type = ForceAtomEnum.KAISER_WEAPON_THROW_1.getForceAtomType();

            switch (tsm.getOption(StopForceAtomInfo).nOption) {
                case 3:
                    inc = ForceAtomEnum.KAISER_WEAPON_THROW_MORPH_1.getInc();
                    type = ForceAtomEnum.KAISER_WEAPON_THROW_MORPH_1.getForceAtomType();
                    break;
                case 2:
                    inc = ForceAtomEnum.KAISER_WEAPON_THROW_2.getInc();
                    type = ForceAtomEnum.KAISER_WEAPON_THROW_2.getForceAtomType();
                    break;
                case 4:
                    inc = ForceAtomEnum.KAISER_WEAPON_THROW_MORPH_2.getInc();
                    type = ForceAtomEnum.KAISER_WEAPON_THROW_MORPH_2.getForceAtomType();
                    break;
            }

            ForceAtomInfo forceAtomInfo = new ForceAtomInfo(1, inc, 25, 30,
                    0, 12 * i, (int) System.currentTimeMillis(), 1, 0,
                    new Position());
            chr.getField().broadcastPacket(CField.createForceAtom(false, 0, chr.getId(), type,
                    true, mob.getObjectId(), Kaiser.TEMPEST_BLADES_FIVE_FF, forceAtomInfo, new Rect(), 0, 300,
                    mob.getPosition(), Kaiser.TEMPEST_BLADES_FIVE_FF, mob.getPosition()));
        }

        tsm.removeStat(StopForceAtomInfo, false);
        tsm.sendResetStatPacket();
    }

    public static void handleSocketCreateRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        inPacket.decodeInt(); // tick
        short uPos = inPacket.decodeShort();
        int itemID = inPacket.decodeInt();
        short ePos = inPacket.decodeShort();
        Item item = chr.getConsumeInventory().getItemBySlot(uPos);
        Equip equip = (Equip) chr.getEquipInventory().getItemBySlot(ePos);
        if (equip == null || item == null || item.getItemId() != itemID) {
            log.error("Unknown equip or mismatching use items.");
            return;
        }
        boolean success = true;
        if (equip.getSockets()[0] == ItemConstants.INACTIVE_SOCKET && ItemConstants.canEquipHavePotential(equip)) {
            chr.consumeItem(item);
            equip.getSockets()[0] = ItemConstants.EMPTY_SOCKET_ID;
        } else {
            success = false;
        }
        c.write(CField.socketCreateResult(success));
        equip.updateToChar(chr);
    }

    public static void handleNebuliteInsertRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        inPacket.decodeInt(); // tick
        short nebPos = inPacket.decodeShort();
        int nebID = inPacket.decodeInt();
        Item item = chr.getInstallInventory().getItemBySlot(nebPos);
        short ePos = inPacket.decodeShort();
        Equip equip = (Equip) chr.getEquipInventory().getItemBySlot(ePos);
        if (item == null || equip == null || item.getItemId() != nebID || !ItemConstants.isNebulite(item.getItemId())) {
            log.error("Nebulite or equip was not found when inserting.");
            chr.dispose();
            return;
        }
        if (equip.getSockets()[0] != ItemConstants.EMPTY_SOCKET_ID) {
            log.error("Tried to Nebulite an item without an empty socket.");
            chr.chatMessage("You can only insert a Nebulite into empty socket slots.");
            chr.dispose();
            return;
        }
        if (!ItemConstants.nebuliteFitsEquip(equip, item)) {
            log.error(String.format("Character %d attempted to use a nebulite (%d) that doesn't fit an equip (%d).", chr.getId(), item.getItemId(), equip.getItemId()));
            chr.chatMessage("The nebulite cannot be mounted on this equip.");
            chr.dispose();
            return;
        }
        chr.consumeItem(item);
        equip.getSockets()[0] = (short) (nebID % ItemConstants.NEBILITE_BASE_ID);
        equip.updateToChar(chr);
    }

    public static void handleUserItemSkillSocketUpdateItemUseRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        inPacket.decodeInt(); // tick
        short uPos = inPacket.decodeShort();
        short ePos = inPacket.decodeShort();
        Item item = chr.getConsumeInventory().getItemBySlot(uPos);
        Equip equip = (Equip) chr.getEquipInventory().getItemBySlot(ePos);
        if (item == null || equip == null || !ItemConstants.isWeapon(equip.getItemId()) ||
                !ItemConstants.isSoulEnchanter(item.getItemId()) || equip.getrLevel() + equip.getiIncReq() < ItemConstants.MIN_LEVEL_FOR_SOUL_SOCKET) {
            chr.dispose();
            return;
        }
        int successProp = ItemData.getItemInfoByID(item.getItemId()).getScrollStats().get(ScrollStat.success);
        boolean success = Util.succeedProp(successProp);
        if (success) {
            equip.setSoulSocketId((short) (item.getItemId() % ItemConstants.SOUL_ENCHANTER_BASE_ID));
            equip.updateToChar(chr);
        }
        chr.getField().broadcastPacket(User.showItemSkillSocketUpgradeEffect(chr.getId(), success));
        chr.consumeItem(item);
    }

    public static void handleUserItemSkillOptionUpdateItemUseRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        inPacket.decodeInt(); // tick
        short uPos = inPacket.decodeShort();
        short ePos = inPacket.decodeShort();
        Item item = chr.getConsumeInventory().getItemBySlot(uPos);
        Equip equip = (Equip) chr.getEquipInventory().getItemBySlot(ePos);
        if (item == null || equip == null || !ItemConstants.isWeapon(equip.getItemId()) ||
                !ItemConstants.isSoul(item.getItemId()) || equip.getSoulSocketId() == 0) {
            chr.dispose();
            return;
        }
        equip.setSoulOptionId((short) (1 + item.getItemId() % ItemConstants.SOUL_ITEM_BASE_ID));
        short option = ItemConstants.getSoulOptionFromSoul(item.getItemId());
        if (option == 0) {
            option = (short) ItemConstants.getRandomSoulOption();
        }
        equip.setSoulOption(option);
        equip.updateToChar(chr);
        chr.consumeItem(item);
        chr.getField().broadcastPacket(User.showItemSkillOptionUpgradeEffect(chr.getId(), true, false));
    }

    public static void handleUserSoulEffectRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        boolean set = inPacket.decodeByte() != 0;
        chr.getField().broadcastPacket(User.SetSoulEffect(chr.getId(), set));
    }

    public static void handleUserWeaponTempItemOptionRequest(Char chr, InPacket inPacket) {
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        if (tsm.hasStat(SoulMP) && tsm.getOption(SoulMP).nOption >= ItemConstants.MAX_SOUL_CAPACITY) {
            Option o = new Option();
            o.nOption = tsm.getOption(SoulMP).nOption;
            o.xOption = tsm.getOption(SoulMP).xOption;
            o.rOption = ItemConstants.getSoulSkillFromSoulID(
                    ((Equip) chr.getEquippedItemByBodyPart(BodyPart.Weapon)).getSoulOptionId()
            );
            tsm.putCharacterStatValue(FullSoulMP, o);
            tsm.sendSetStatPacket();
        }
        chr.dispose();
    }

    public static void handleUserMigrateToCashShopRequest(Client c, InPacket inPacket) {
        CashShop cs = Server.getInstance().getCashShop();
        c.write(Stage.setCashShop(c.getChr(), cs));
        c.write(CCashShop.loadLockerDone(c.getChr().getAccount()));
        c.write(CCashShop.queryCashResult(c.getChr()));
        c.write(CCashShop.bannerInfo(cs));
        c.write(CCashShop.cartInfo(cs));
        c.write(CCashShop.featuredItemInfo(cs));
        c.write(CCashShop.specialItemInfo(cs));
        c.write(CCashShop.specialSaleInfo(cs));
        c.write(CCashShop.topSellerInfo(cs));
        c.write(CCashShop.categoryInfo(cs));
        c.write(CCashShop.bannerMsg(cs, new ArrayList<>(Arrays.asList("Welcome to SwordieMS!", "Enjoy your time here."))));
        c.write(CCashShop.oneTen(cs));
    }


    public static void handleMonsterBookMobInfo(Char chr, InPacket inPacket) {
        inPacket.decodeInt(); // tick
        int cardID = inPacket.decodeInt();
        ItemInfo ii = ItemData.getItemInfoByID(cardID);
        Mob mob = MobData.getMobById(ii.getMobID());
        if (mob != null) {
            // TODO Figure out which packet to send
        }
    }

    public static void handleGachaponRequest(Char chr, InPacket inPacket) {
        // TODO: Handle error messages with popup dialog like GMS.
        // TODO: Add rewards to gachapon.
        final int type = inPacket.decodeByte();
        final GachaponResult result = GachaponResult.getByVal(type);

        if (result == null) {
            log.error("[Gachapon] Found unknown gachapon result " + type);
            chr.write(GachaponDlg.gachResult(GachaponResult.ERROR));
            return;
        }
        if (chr == null) {
            chr.write(GachaponDlg.gachResult(GachaponResult.ERROR));
            return;
        }
        switch (result) {
            case SUCCESS:
                final int ticketID = inPacket.decodeInt();
                GachaponDlgType dialog = GachaponConstants.getDlgByTicket(ticketID);
                if (dialog == null || !chr.hasItem(ticketID)) {
                    chr.write(GachaponDlg.gachResult(GachaponResult.ERROR));
                    return;
                }
                final int reward = GachaponConstants.getRandomItem(dialog);
                if (reward == -1) {
                    chr.chatMessage(ChatType.Mob, "Cannot find reward ID");
                    chr.write(GachaponDlg.gachResult(GachaponResult.ERROR));
                    return;
                }
                if (!chr.canHold(reward)) {
                    chr.chatMessage(ChatType.Mob, "Cannot hold reward ID " + reward);
                    chr.write(GachaponDlg.gachResult(GachaponResult.ERROR));
                    return;
                }
                Equip equip = ItemData.getEquipDeepCopyFromID(reward, true);
                if (equip == null) {
                    Item item = ItemData.getItemDeepCopy(reward, true);
                    if (item == null) {
                        chr.write(GachaponDlg.gachResult(GachaponResult.ERROR));
                        chr.chatMessage(ChatType.Mob, "Item is null" + reward);
                        return;
                    }
                    item.setQuantity(1);
                    chr.addItemToInventory(item);
                    chr.write(GachaponDlg.gachResult(GachaponResult.SUCCESS, item, (short) 1));
                    chr.getGachaponManager().addItem(dialog, item, (short) 1);
                } else {
                    chr.addItemToInventory(InvType.EQUIP, equip, false);
                    chr.write(GachaponDlg.gachResult(GachaponResult.SUCCESS, equip, (short) 1));
                    chr.getGachaponManager().addItem(dialog, equip, (short) 1);
                }
                chr.consumeItem(chr.getCashInventory().getItemByItemID(ticketID));
                chr.getField().broadcastPacket(User.setGachaponEffect(chr));
                break;
            case EXIT:
                chr.write(GachaponDlg.gachResult(GachaponResult.EXIT));
                break;
        }
    }

    public static void handleUserRequestChangeMobZoneState(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        int mobID = inPacket.decodeInt();
        Position pos = inPacket.decodePositionInt();
        Life life = chr.getField().getLifeByObjectID(mobID);
        if (life instanceof Mob) {
            Mob mob = (Mob) life;
            // Should this be handled like this? I doubt it, but it works :D
            int dataType = 0;
            switch (life.getTemplateId()) {
                case 8880002: // Normal magnus
                    double perc = mob.getHp() / (double) mob.getMaxHp();
                    if (perc <= 0.25) {
                        dataType = 4;
                    } else if (perc <= 0.5) {
                        dataType = 3;
                    } else if (perc <= 0.75) {
                        dataType = 2;
                    } else {
                        dataType = 1;
                    }
                    break;
                default:
                    log.error("Unhandled mob zone stat for mob template id " + life.getTemplateId());
            }
            chr.getField().broadcastPacket(CField.changeMobZone(mobID, dataType));
        }
        OutPacket outPacket = new OutPacket(OutHeader.SERVER_ACK_MOB_ZONE_STATE_CHANGE);
        outPacket.encodeByte(true);
        c.write(outPacket);
    }

    public static void handleNpcMove(Char chr, InPacket inPacket) {
        int objectID = inPacket.decodeInt();
        byte oneTimeAction = inPacket.decodeByte();
        byte chatIdx = inPacket.decodeByte();
        int duration = inPacket.decodeInt();
        Life life = chr.getField().getLifeByObjectID(objectID);
        if (life instanceof Npc && ((Npc) life).isMove()) {
            Npc npc = (Npc) chr.getField().getLifeByObjectID(objectID);
            boolean move = npc.isMove();
            MovementInfo movementInfo = new MovementInfo(npc.getPosition(), npc.getVPosition());
            byte keyPadState = 0;
            if (move) {
                movementInfo.decode(inPacket);
                for (Movement m : movementInfo.getMovements()) {
                    Position pos = m.getPosition();
                    Position vPos = m.getVPosition();
                    if (pos != null) {
                        npc.setPosition(pos);
                    }
                    if (vPos != null) {
                        npc.setvPosition(vPos);
                    }
                    npc.setMoveAction(m.getMoveAction());
                    npc.setFh(m.getFh());
                }
                if (inPacket.getUnreadAmount() > 0) {
                    keyPadState = inPacket.decodeByte(); // not always encoded?
                }
            }
            chr.getField().broadcastPacket(NpcPool.npcMove(objectID, oneTimeAction, chatIdx, duration, move,
                    movementInfo, keyPadState));
        }
    }

    public static void handleUserProtectBuffDieItemRequest(Char chr, InPacket inPacket) {
        inPacket.decodeInt(); // tick
        boolean used = inPacket.decodeByte() != 0;
        if (used) {
            // grabs the first one from the list of buffItems
            Item buffProtector = chr.getBuffProtectorItem();
            if (buffProtector != null) {
                chr.setBuffProtector(true);
                chr.consumeItem(buffProtector);
                chr.write(UserLocal.setBuffProtector(buffProtector.getItemId(), true));
            } else {
                log.error(String.format("Character id %d tried to use a buff without having the appropriate item.", chr.getId()));
            }
        }
    }

    public static void handleUserRequestStealSkillList(Client c, InPacket inPacket) {
        int targetChrID = inPacket.decodeInt();

        Char chr = c.getChr();
        Char targetChr = chr.getField().getCharByID(targetChrID);
        Set<Skill> targetSkillsList = targetChr.getSkills();

        chr.write(UserLocal.resultStealSkillList(targetSkillsList, 4, targetChrID, targetChr.getJob()));
        chr.dispose();
    }

    public static void handleUserRequestStealSkillMemory(Client c, InPacket inPacket) {
        int stealSkillID = inPacket.decodeInt();
        int targetChrID = inPacket.decodeInt();
        boolean add = inPacket.decodeByte() != 0;   // 0 = add  |  1 = remove

        Char chr = c.getChr();
        Char targetChr = c.getChr().getField().getCharByID(targetChrID);

        Skill stolenSkill = SkillData.getSkillDeepCopyById(stealSkillID);
        int stealSkillMaxLv = stolenSkill.getMasterLevel();
        int stealSkillCurLv = targetChr == null ? stealSkillMaxLv : targetChr.getSkill(stealSkillID).getCurrentLevel(); //TODO this is for testing,  needs to be:    targetChr.getSkillID(stealSkillID).getCurrentLevel();

        if (!add) {
            // /Add Stolen Skill

            if (chr.getStolenSkillBySkillId(stealSkillID) != null) {
                chr.chatMessage("You already have this stolen skill.");
                chr.dispose();
                return;
            }

            int position = StolenSkill.getFirstEmptyPosition(chr, stealSkillID);
            if (position == -1) {
                chr.chatMessage("Could not continue with stealing skills due to an unknown error.");
                chr.dispose();
                return;
            }
            StolenSkill.setSkill(chr, stealSkillID, position, (byte) stealSkillCurLv);

            int positionPerTab = StolenSkill.getPositionForTab(position, stealSkillID);
            c.write(UserLocal.changeStealMemoryResult(STEAL_SKILL.getVal(), SkillConstants.getStealSkillManagerTabFromSkill(stealSkillID), positionPerTab, stealSkillID, stealSkillCurLv, stealSkillMaxLv));
        } else {
            //Remove Stolen Skill
            int position = StolenSkill.getPositionPerTabFromStolenSkill(chr.getStolenSkillBySkillId(stealSkillID));
            StolenSkill.removeSkill(chr, stealSkillID);
            c.write(UserLocal.changeStealMemoryResult(REMOVE_STEAL_MEMORY.getVal(), SkillConstants.getStealSkillManagerTabFromSkill(stealSkillID), position, stealSkillID, stealSkillCurLv, stealSkillMaxLv));
        }
        chr.dispose();
    }

    public static void handleUserRequestSetStealSkillSlot(Client c, InPacket inPacket) {
        int impeccableSkillID = inPacket.decodeInt();
        int stealSkillID = inPacket.decodeInt();

        ChosenSkill.setChosenSkill(c.getChr(), stealSkillID, impeccableSkillID);
        c.write(UserLocal.resultSetStealSkill(true, impeccableSkillID, stealSkillID));
        c.getChr().dispose();
    }

    public static void handleUserExItemUpgradeItemUseRequest(Client c, InPacket inPacket) {
        inPacket.decodeInt(); //tick
        short usePosition = inPacket.decodeShort(); //Use Position
        short eqpPosition = inPacket.decodeShort(); //Equip Position
        byte echantSkill = inPacket.decodeByte(); //boolean

        Char chr = c.getChr();
        Item flame = chr.getInventoryByType(InvType.CONSUME).getItemBySlot(usePosition);
        InvType invType = eqpPosition < 0 ? EQUIPPED : EQUIP;
        Equip equip = (Equip) chr.getInventoryByType(invType).getItemBySlot(eqpPosition);

        if (flame == null || equip == null) {
            chr.chatMessage(SystemNotice, "Could not find flame or equip.");
            chr.dispose();
            return;
        }

        if (!ItemConstants.isRebirthFlame(flame.getItemId())) {
            chr.chatMessage(SystemNotice, "This item is not a rebirth flame.");
            chr.dispose();
            return;
        }

        Map<ScrollStat, Integer> vals = ItemData.getItemInfoByID(flame.getItemId()).getScrollStats();

        if (vals.size() > 0) {
            int reqEquipLevelMax = vals.getOrDefault(ScrollStat.reqEquipLevelMax, 250);

            if (equip.getrLevel() + equip.getiIncReq() > reqEquipLevelMax) {
                c.write(WvsContext.broadcastMsg(BroadcastMsg.popUpMessage("Equipment level does not meet scroll requirements.")));
                chr.dispose();
                return;
            }

            boolean success = Util.succeedProp(vals.getOrDefault(ScrollStat.success, 100));

            if (success) {
                boolean eternalFlame = vals.getOrDefault(ScrollStat.createType, 6) >= 7;
                equip.randomizeFlameStats(eternalFlame); // Generate high stats if it's an eternal/RED flame only.
            }

            c.write(CField.showItemUpgradeEffect(chr.getId(), success, false, flame.getItemId(), equip.getItemId(), false));
            equip.updateToChar(chr);
            chr.consumeItem(flame);
        }

        chr.dispose();
    }

    public static void handleUserRequestCharacterPotentialSkillRandSetUi(Char chr, InPacket inPacket) {
        // what a name
        int cost = GameConstants.CHAR_POT_RESET_COST;
        int rate = inPacket.decodeInt();
        int size = inPacket.decodeInt();
        Set<Integer> lockedLines = new HashSet<>();
        for (int i = 0; i < size; i++) {
            lockedLines.add(inPacket.decodeInt());
            if (lockedLines.size() == 0) {
                cost += GameConstants.CHAR_POT_LOCK_1_COST;
            } else {
                cost += GameConstants.CHAR_POT_LOCK_2_COST;
            }
        }
        boolean locked = rate > 0;
        if (locked) {
            cost += GameConstants.CHAR_POT_GRADE_LOCK_COST;
        }
        if (cost > chr.getHonorExp()) {
            chr.chatMessage("You do not have enough honor exp for that action.");
            log.warn(String.format("Character %d tried to reset honor without having enough exp (required %d, has %d)",
                    chr.getId(), cost, chr.getHonorExp()));
            return;
        }
        chr.addHonorExp(-cost);

        CharacterPotentialMan cpm = chr.getPotentialMan();
        boolean gradeUp = !locked && Util.succeedProp(GameConstants.BASE_CHAR_POT_UP_RATE);
        boolean gradeDown = !locked && Util.succeedProp(GameConstants.BASE_CHAR_POT_DOWN_RATE);
        byte grade = cpm.getGrade();
        // update grades
        if (grade < CharPotGrade.Legendary.ordinal() && gradeUp) {
            grade++;
        } else if (grade > CharPotGrade.Rare.ordinal() && gradeDown) {
            grade--;
        }
        // set new potentials that weren't locked
        for (CharacterPotential cp : chr.getPotentials()) {
            cp.setGrade(grade);
            if (!lockedLines.contains((int) cp.getKey())) {
                cpm.addPotential(cpm.generateRandomPotential(cp.getKey()));
            }
        }
    }

    public static void handleUserCashPetPickUpOnOffRequest(Char chr, InPacket inPacket) {
        inPacket.decodeInt(); // tick
        boolean on = inPacket.decodeByte() != 0;
        boolean channelChange = inPacket.decodeByte() != 0;
        int attribute = 0;
        if (channelChange) {
            attribute = inPacket.decodeInt();
        }
        chr.write(WvsContext.cashPetPickUpOnOffResult(true, on));

    }

    public static void handleRuneStoneUseRequest(Client c, InPacket inPacket) {
        int unknown = inPacket.decodeInt(); // unknown
        RuneType runeType = RuneType.getByVal((byte) inPacket.decodeInt());

        Char chr = c.getChr();
        int minLevel = chr.getField().getMobs().stream().mapToInt(m -> m.getForcedMobStat().getLevel()).min().orElse(0);

        // User is on RuneStone Cooldown
        if ((c.getChr().getRuneCooldown() + (GameConstants.RUNE_COOLDOWN_TIME * 60000)) < System.currentTimeMillis()) {

            // Rune is too strong for user
            if (minLevel > c.getChr().getStat(Stat.level)) {
                c.write(CField.runeStoneUseAck(4));
                return;
            }

            // Send Arrow Message
            c.write(CField.runeStoneUseAck(5));
        } else {
            long minutes = (((c.getChr().getRuneCooldown() + (GameConstants.RUNE_COOLDOWN_TIME * 60000)) - System.currentTimeMillis()) / 60000);
            long seconds = (((c.getChr().getRuneCooldown() + (GameConstants.RUNE_COOLDOWN_TIME * 60000)) - System.currentTimeMillis()) / 1000);
            chr.chatScriptMessage("You cannot use another Rune for " +
                    (minutes > 0 ?
                            minutes + " minute" + (minutes > 1 ? "s" : "") + " and " + (seconds - (minutes * 60)) + " second" + ((seconds - (minutes * 60)) > 1 ? "s" : "") + "" :
                            seconds + " second" + (seconds > 1 ? "s" : "")
                    ));
        }
        chr.dispose();
    }

    public static void handleRuneStoneSkillRequest(Client c, InPacket inPacket) {
        boolean success = inPacket.decodeByte() != 0; //Successfully done the Arrow Shit for runes

        if (success) {
            RuneStone runeStone = c.getChr().getField().getRuneStone();

            c.getChr().getField().useRuneStone(c, runeStone);
            //c.write(CField.runeStoneSkillAck(runeStone.getRuneType()));
            runeStone.activateRuneStoneEffect(c.getChr());
            c.getChr().setRuneCooldown(System.currentTimeMillis());
        }
        c.getChr().dispose();
    }

    public static void handleSetSonOfLinkedSkillRequest(Char chr, InPacket inPacket) {
        int skillID = inPacket.decodeInt();
        int sonID = inPacket.decodeInt();
        short jobID = chr.getJob();
        Account acc = chr.getAccount();
        Char son = acc.getCharacters().stream().filter(c -> c.getId() == sonID).findAny().orElse(null);
        // remove old link skill if another with the same skill exists
        acc.getLinkSkills().stream()
                .filter(ls -> SkillConstants.getOriginalOfLinkedSkill(ls.getLinkSkillID()) == skillID)
                .findAny()
                .ifPresent(oldLinkSkill -> acc.removeLinkSkillByOwnerID(oldLinkSkill.getOwnerID()));
        // if the skill is not null and we expect this link skill id from the given job
        int linkSkillID = SkillConstants.getLinkSkillByJob(jobID);
        if (son != null && SkillConstants.getOriginalOfLinkedSkill(linkSkillID) == skillID) {
            acc.addLinkSkill(chr, sonID, linkSkillID);
            chr.write(WvsContext.setSonOfLinkedSkillResult(LinkedSkillResultType.SetSonOfLinkedSkillResult_Success,
                    son.getId(), son.getName(), skillID, null));
        } else {
            chr.write(WvsContext.setSonOfLinkedSkillResult(LinkedSkillResultType.SetSonOfLinkedSkillResult_Fail_Unknown,
                    0, null, 0, null));
        }
    }

    public static void handleUserMemorialCubeOptionRequest(Char chr, InPacket inPacket) {
        inPacket.decodeInt(); // tick
        MemorialCubeInfo mci = chr.getMemorialCubeInfo();
        boolean chooseBefore = inPacket.decodeByte() == 7;
        if (chooseBefore) {
            Inventory equipInv = chr.getEquipInventory();
            Equip equip = mci.getOldEquip();
            Equip invEquip = (Equip) equipInv.getItemBySlot((short) equip.getBagIndex());
            equipInv.removeItem(invEquip);
            equipInv.addItem(equip);
            equip.updateToChar(chr);
        }
        chr.setMemorialCubeInfo(null);
    }

    public static void handleFamiliarAddRequest(Char chr, InPacket inPacket) {
        inPacket.decodeInt(); // tick
        short slot = inPacket.decodeShort();
        int itemID = inPacket.decodeInt();
        Item item = chr.getConsumeInventory().getItemBySlot(slot);
        if (item == null || item.getItemId() != itemID || !ItemConstants.isFamiliar(itemID)) {
            chr.chatMessage("Could not find that item.");
            log.error(String.format("Character %d tried to add a familiar it doesn't have. (item id %d)", chr.getId(), itemID));
        }
        int suffix = itemID % 10000;
        int familiarID = (ItemConstants.FAMILIAR_PREFIX * 10000) + suffix;
        Familiar familiar = chr.getFamiliarByID(familiarID);
        boolean showInfo = true;
        if (familiar == null) {
            familiar = new Familiar(0, familiarID, "Familiar", FileTime.fromType(FileTime.Type.MAX_TIME), (short) 1);
            showInfo = false;
            chr.addFamiliar(familiar);
        } else {
            familiar.setVitality((short) Math.min(familiar.getVitality() + 1, 3));
        }
        chr.write(UserLocal.familiarAddResult(familiar, showInfo, false));
    }

    public static void handleFamiliarSpawnRequest(Char chr, InPacket inPacket) {
        inPacket.decodeInt(); // tick
        int familiarID = inPacket.decodeInt();
        boolean on = inPacket.decodeByte() != 0;
        Familiar familiar = chr.getFamiliarByID(familiarID);
        if (familiar != null) {
            if (chr.getActiveFamiliar() != null && chr.getActiveFamiliar() != familiar) {
                chr.getField().broadcastPacket(CFamiliar.familiarEnterField(chr.getId(), false,
                        chr.getActiveFamiliar(), false, true));
            }
            chr.setActiveFamiliar(on ? familiar : null);
            if (on) {
                familiar.setPosition(chr.getPosition().deepCopy());
                familiar.setFh(chr.getFoothold());
            }
            chr.getField().broadcastPacket(CFamiliar.familiarEnterField(chr.getId(), false, familiar, on, true));
        }
        chr.dispose();
    }

    public static void handleFamiliarRenameRequest(Char chr, InPacket inPacket) {
        int familiarID = inPacket.decodeInt();
        String name = inPacket.decodeString();
        if (name.length() > 13) {
            name = name.substring(0, 13);
        }
        Familiar familiar = chr.getFamiliarByID(familiarID);
        if (familiar != null) {
            familiar.setName(name);
        }
        chr.dispose();
    }

    public static void handleFamiliarMove(Char chr, InPacket inPacket) {
        inPacket.decodeByte(); // ?
        inPacket.decodeInt(); // familiar id
        Life life = chr.getActiveFamiliar();
        if (life instanceof Familiar) {
            MovementInfo movementInfo = new MovementInfo(inPacket);
            movementInfo.applyTo(life);
            chr.getField().broadcastPacket(CFamiliar.familiarMove(chr.getId(), movementInfo), chr);
        }
    }

    public static void handleFamiliarAttack(Char chr, InPacket inPacket) {
        inPacket.decodeByte(); // ?
        int familiarID = inPacket.decodeInt();
        if (chr.getActiveFamiliar() == null || chr.getActiveFamiliar().getFamiliarID() != familiarID) {
            return;
        }
        AttackInfo ai = new AttackInfo();
        ai.attackHeader = OutHeader.FAMILIAR_ATTACK;
        ai.fieldKey = inPacket.decodeByte();
        ai.skillId = inPacket.decodeInt();
        ai.idk = inPacket.decodeByte();
        ai.slv = 1;
        ai.mobCount = inPacket.decodeByte();
        for (int i = 0; i < ai.mobCount; i++) {
            MobAttackInfo mai = new MobAttackInfo();
            mai.mobId = inPacket.decodeInt();
            mai.byteIdk1 = inPacket.decodeByte();
            mai.byteIdk2 = inPacket.decodeByte();
            mai.byteIdk3 = inPacket.decodeByte();
            mai.byteIdk4 = inPacket.decodeByte();
            mai.byteIdk5 = inPacket.decodeByte();
            int idk1 = inPacket.decodeInt();
            byte idk2 = inPacket.decodeByte();
            int idk3 = inPacket.decodeInt();
            mai.damages = new int[inPacket.decodeByte()];
            for (int j = 0; j < mai.damages.length; j++) {
                mai.damages[j] = inPacket.decodeInt();
            }
            ai.mobAttackInfo.add(mai);
        }
        handleAttack(chr.getClient(), ai);
        // 4 more bytes after this, not sure what it is
    }

    public static void handleUserThrowGrenade(Char chr, InPacket inPacket) {
        Position pos = inPacket.decodePositionInt();
        int y = inPacket.decodeInt(); // another y?
        int keyDown = inPacket.decodeInt();
        int skillID = inPacket.decodeInt();
        int bySummonedID = inPacket.decodeInt(); // slv according to ida, but let's just take that server side
        boolean left = inPacket.decodeByte() != 0;
        int attackSpeed = inPacket.decodeInt();
        int grenadeID = inPacket.decodeInt();
        Skill skill = chr.getSkill(SkillConstants.getLinkedSkill(skillID));
        int slv = skill == null ? 0 : skill.getCurrentLevel();
        if (slv == 0) {
            log.error(String.format("Character %d tried to make a grenade with a skill they do not possess (id %d)", chr.getId(), skillID));
        } else {
            boolean success = true;
            if (SkillData.getSkillInfoById(skillID).hasCooltime()) {
                if (chr.hasSkillOnCooldown(skillID)) {
                    success = false;
                } else {
                    chr.setSkillCooldown(skillID, (byte) slv);
                }
            }
            if (success) {
                chr.getField().broadcastPacket(UserRemote.throwGrenade(chr.getId(), grenadeID, pos, keyDown, skillID,
                        bySummonedID, slv, left, attackSpeed), chr);
            }
        }

    }

    public static void handleUserDestroyGrenade(Char chr, InPacket inPacket) {
        int grenadeID = inPacket.decodeInt();
        int skillID = inPacket.decodeInt();
        chr.getField().broadcastPacket(UserRemote.destroyGrenade(chr.getId(), grenadeID), chr);
    }

    public static void handleAttack(Char chr, InPacket inPacket, InHeader header) {
        AttackInfo ai = new AttackInfo();
        switch (header) {
            case USER_MELEE_ATTACK:
                ai.attackHeader = OutHeader.REMOTE_MELEE_ATTACK;
                break;
            case USER_SHOOT_ATTACK:
                ai.boxAttack = inPacket.decodeByte() != 0; // hardcoded 0
                ai.attackHeader = OutHeader.REMOTE_SHOOT_ATTACK;
                break;
            case USER_NON_TARGET_FORCE_ATOM_ATTACK:
                inPacket.decodeArr(12); // id/crc/something else
            case USER_MAGIC_ATTACK:
                ai.attackHeader = OutHeader.REMOTE_MAGIC_ATTACK;
                break;

        }
        ai.bulletID = chr.getBulletIDForAttack();
        ai.fieldKey = inPacket.decodeByte();
        byte mask = inPacket.decodeByte();
        ai.hits = (byte) (mask & 0xF);
        ai.mobCount = (mask >>> 4) & 0xF;
        ai.skillId = inPacket.decodeInt();
        ai.slv = inPacket.decodeByte();
        if (header == InHeader.USER_MELEE_ATTACK || header == InHeader.USER_SHOOT_ATTACK
                || header == InHeader.USER_NON_TARGET_FORCE_ATOM_ATTACK) {
            ai.addAttackProc = inPacket.decodeByte();
        }
        inPacket.decodeInt(); // crc
        int skillID = ai.skillId;
        if (SkillConstants.isKeyDownSkill(skillID) || SkillConstants.isSuperNovaSkill(skillID)) {
            ai.keyDown = inPacket.decodeInt();
        }
        if (SkillConstants.isRushBombSkill(skillID) || skillID == 5300007 || skillID == 27120211 || skillID == 14111023) {
            ai.grenadeId = inPacket.decodeInt();
        }
        if (SkillConstants.isZeroSkill(skillID)) {
            ai.zero = inPacket.decodeByte();
        }
        if (SkillConstants.isUsercloneSummonedAbleSkill(skillID)) {
            ai.bySummonedID = inPacket.decodeInt();
        }
        ai.buckShot = inPacket.decodeByte();
        ai.someMask = inPacket.decodeByte();
        if (header == InHeader.USER_SHOOT_ATTACK) {
            int idk3 = inPacket.decodeInt();
            ai.isJablin = inPacket.decodeByte() != 0;
            if (ai.boxAttack) {
                int boxIdk1 = inPacket.decodeInt();
                short boxIdk2 = inPacket.decodeShort();
                short boxIdk3 = inPacket.decodeShort();
            }
        }
        short maskie = inPacket.decodeShort();
        ai.left = ((maskie >>> 15) & 1) != 0;
        ai.attackAction = (short) (maskie & 0x7FFF);
        ai.requestTime = inPacket.decodeInt();
        ai.attackActionType = inPacket.decodeByte();
        if (skillID == 23111001 || skillID == 80001915 || skillID == 36111010) {
            int idk5 = inPacket.decodeInt();
            int x = inPacket.decodeInt(); // E0 6E 1F 00
            int y = inPacket.decodeInt();
        }
        if (SkillConstants.isEvanForceSkill(skillID)) {
            ai.idk0 = inPacket.decodeByte();
        }
        ai.attackSpeed = inPacket.decodeByte();
        ai.tick = inPacket.decodeInt();
        if (header != InHeader.USER_MAGIC_ATTACK && header != InHeader.USER_NON_TARGET_FORCE_ATOM_ATTACK) {
            int bulletSlot = inPacket.decodeInt();
        }
        if (header == InHeader.USER_MELEE_ATTACK) {
            ai.finalAttackLastSkillID = inPacket.decodeInt();
            if (ai.finalAttackLastSkillID > 0) {
                ai.finalAttackByte = inPacket.decodeByte();
            }
        } else if (header == InHeader.USER_MAGIC_ATTACK || header == InHeader.USER_NON_TARGET_FORCE_ATOM_ATTACK) {
            int idk = inPacket.decodeInt();
        } else {
            short idk8 = inPacket.decodeShort();
            short idk9 = inPacket.decodeShort();
        }
        if (header == InHeader.USER_NON_TARGET_FORCE_ATOM_ATTACK) {
            inPacket.decodeInt(); // hardcoded 0
        }
        if (header == InHeader.USER_SHOOT_ATTACK) {
            // this looks correct in ida, but completely wrong when comparing it to the actual packet
//            ai.shootRange = inPacket.decodeByte();
//            if (!SkillConstants.isShootSkillNotConsumingBullets(skillID)
//                    || chr.getTemporaryStatManager().hasStat(CharacterTemporaryStat.SoulArrow)) {
//                ai.bulletCount = inPacket.decodeInt();
//            }
            int bulletSlot = inPacket.decodeInt();
            byte idk = inPacket.decodeByte();
            if ((bulletSlot == 0 || idk == 0) && (ai.buckShot & 0x40) != 0 && !SkillConstants.isFieldAttackObjSkill(skillID)) {
                int maybeID = inPacket.decodeInt();
            }
            ai.rect = inPacket.decodeShortRect();
        }
        if (skillID == 5111009) {
            ai.ignorePCounter = inPacket.decodeByte() != 0;
        }
        /*if ( v1756 )
          {
            COutPacket::Encode2(&a, v1747);
            if ( v674 || is_noconsume_usebullet_melee_attack(v669) )
              COutPacket::Encode4(&a, v1748);
          }*/
        if (skillID == 25111005) {
            ai.spiritCoreEnhance = inPacket.decodeInt();
        }
        for (int i = 0; i < ai.mobCount; i++) {
            MobAttackInfo mai = new MobAttackInfo();
            mai.mobId = inPacket.decodeInt();
            mai.hitAction = inPacket.decodeByte();
            mai.left = inPacket.decodeByte();
            mai.idk3 = inPacket.decodeByte();
            mai.forceActionAndLeft = inPacket.decodeByte();
            mai.frameIdx = inPacket.decodeByte();
            mai.templateID = inPacket.decodeInt();
            mai.calcDamageStatIndexAndDoomed = inPacket.decodeByte(); // 1st bit for bDoomed, rest for calcDamageStatIndex
            mai.hitX = inPacket.decodeShort();
            mai.hitY = inPacket.decodeShort();
            mai.oldPosX = inPacket.decodeShort(); // ?
            mai.oldPosY = inPacket.decodeShort(); // ?
            if (header == InHeader.USER_MAGIC_ATTACK) {
                mai.hpPerc = inPacket.decodeByte();
                if (skillID == 80001835) {
                    mai.magicInfo = (short) inPacket.decodeByte();
                } else {
                    mai.magicInfo = inPacket.decodeShort();
                }
            } else {
                mai.idk6 = inPacket.decodeShort();
            }
            mai.damages = new int[ai.hits];
            for (int j = 0; j < ai.hits; j++) {
                mai.damages[j] = inPacket.decodeInt();
            }
            mai.mobUpDownYRange = inPacket.decodeInt();
            inPacket.decodeInt(); // crc
            if (skillID == 37111005) {
                mai.isResWarriorLiftPress = inPacket.decodeByte() != 0;
            }
            // Begin PACKETMAKER::MakeAttackInfoPacket
            mai.type = inPacket.decodeByte();
            mai.currentAnimationName = "";
            if (mai.type == 1) {
                mai.currentAnimationName = inPacket.decodeString();
                mai.animationDeltaL = inPacket.decodeInt();
                mai.hitPartRunTimesSize = inPacket.decodeInt();
                mai.hitPartRunTimes = new String[mai.hitPartRunTimesSize];
                for (int j = 0; j < mai.hitPartRunTimesSize; j++) {
                    mai.hitPartRunTimes[j] = inPacket.decodeString();
                }
            } else if (mai.type == 2) {
                mai.currentAnimationName = inPacket.decodeString();
                mai.animationDeltaL = inPacket.decodeInt();
            }
            // End PACKETMAKER::MakeAttackInfoPacket
            ai.mobAttackInfo.add(mai);
        }
        if (skillID == 61121052 || skillID == 36121052 || SkillConstants.isScreenCenterAttackSkill(skillID)) {
            ai.ptTarget.setX(inPacket.decodeShort());
            ai.ptTarget.setY(inPacket.decodeShort());
        } else {
            if (skillID == 27121052 || skillID == 80001837) {
                ai.x = inPacket.decodeShort();
                ai.y = inPacket.decodeShort();
            }
            if (header == InHeader.USER_MAGIC_ATTACK && skillID != 3211016) {
                short forcedX = inPacket.decodeShort();
                short forcedY = inPacket.decodeShort();
                boolean dragon = inPacket.decodeByte() != 0;
                ai.forcedX = forcedX;
                ai.forcedY = forcedY;
                if (dragon) {
                    short rcDstRight = inPacket.decodeShort();
                    short rectRight = inPacket.decodeShort();
                    short x = inPacket.decodeShort();
                    short y = inPacket.decodeShort();
                    inPacket.decodeByte(); // always 0
                    inPacket.decodeByte(); // -1
                    inPacket.decodeByte(); // 0
                    ai.rcDstRight = rcDstRight;
                    ai.rectRight = rectRight;
                    ai.x = x;
                    ai.y = y;
                }
            }
            if (skillID == BlazeWizard.IGNITION_EXPLOSION) {
                ai.option = inPacket.decodeInt();
            }
            if (skillID == Magician.MIST_ERUPTION) {
                byte size = inPacket.decodeByte();
                int[] mists = new int[size];
                for (int i = 0; i < size; i++) {
                    mists[i] = inPacket.decodeInt();
                }
                ai.mists = mists;
            }
            if (skillID == Magician.POISON_MIST) {
                byte force = inPacket.decodeByte();
                short forcedXSh = inPacket.decodeShort();
                short forcedYSh = inPacket.decodeShort();
                ai.force = force;
                ai.forcedXSh = forcedXSh;
                ai.forcedYSh = forcedYSh;
            }
            if (skillID == 80001835) { // Soul Shear
                byte sizeB = inPacket.decodeByte();
                int[] idkArr2 = new int[sizeB];
                short[] shortArr2 = new short[sizeB];
                for (int i = 0; i < sizeB; i++) {
                    idkArr2[i] = inPacket.decodeInt();
                    shortArr2[i] = inPacket.decodeShort();
                }
                short delay = inPacket.decodeShort();
                ai.mists = idkArr2;
                ai.shortArr = shortArr2;
                ai.delay = delay;
            }
            if (SkillConstants.isSuperNovaSkill(skillID)) {
                ai.ptAttackRefPoint.setX(inPacket.decodeShort());
                ai.ptAttackRefPoint.setY(inPacket.decodeShort());
            }
            if (skillID == 101000102) { // Air Riot
                ai.idkPos = inPacket.decodePosition();
            }
            if (header == InHeader.USER_AREA_DOT_ATTACK) {
                ai.pos.setX(inPacket.decodeShort());
                ai.pos.setY(inPacket.decodeShort());
            }
            if (SkillConstants.isAranFallingStopSkill(skillID)) {
                ai.fh = inPacket.decodeByte();
            }
            if (header == InHeader.USER_SHOOT_ATTACK && skillID / 1000000 == 33) {
                ai.bodyRelMove = inPacket.decodePosition();
            }
            if (skillID == 21120019 || skillID == 37121052) {
                ai.teleportPt.setX(inPacket.decodeInt());
                ai.teleportPt.setY(inPacket.decodeInt());
            }
            if (header == InHeader.USER_SHOOT_ATTACK && SkillConstants.isKeydownSkillRectMoveXY(skillID)) {
                ai.keyDownRectMoveXY = inPacket.decodePosition();
            }
            if (skillID == 61121105 || skillID == 61121222 || skillID == 24121052) {
                ai.Vx = inPacket.decodeShort();
                short x, y;
                for (int i = 0; i < ai.Vx; i++) {
                    x = inPacket.decodeShort();
                    y = inPacket.decodeShort();
                }
            }
            if (skillID == 101120104) {
                // CUser::EncodeAdvancedEarthBreak
                // TODO
            }
            if (skillID == 14111006 && ai.grenadeId != 0) {
                ai.grenadePos.setX(inPacket.decodeShort());
                ai.grenadePos.setY(inPacket.decodeShort());
            }
            if (/*skillID == 23121002 ||*/ skillID == 80001914) { // first skill is Spikes Royale, not needed?
                ai.fh = inPacket.decodeByte();
            }
        }
        handleAttack(chr.getClient(), ai);
    }

    public static void handleUserHyperSkillUpRequest(Char chr, InPacket inPacket) {
        // TODO: verify hyper skill is of the character's class
        inPacket.decodeInt(); // tick
        int skillID = inPacket.decodeInt();
        SkillInfo si = SkillData.getSkillInfoById(skillID);
        if (si == null) {
            log.error(String.format("Character %d attempted assigning hyper SP to a skill with null skillinfo (%d).", chr.getId(), skillID));
            chr.dispose();
            return;
        }
        if (si.getHyper() == 0 && si.getHyperStat() == 0) {
            log.error(String.format("Character %d attempted assigning hyper SP to a wrong skill (skill id %d, player job %d)", chr.getId(), skillID, chr.getJob()));
            chr.dispose();
            return;
        }
        Skill skill = chr.getSkill(skillID, true);
        if (si.getHyper() != 0) { // Passive hyper
            ExtendSP esp = chr.getAvatarData().getCharacterStat().getExtendSP();
            SPSet spSet = si.getHyper() == 1 ? esp.getSpSet().get(SkillConstants.PASSIVE_HYPER_JOB_LEVEL - 1) :
                    esp.getSpSet().get(SkillConstants.ACTIVE_HYPER_JOB_LEVEL - 1);
            int curSp = spSet.getSp();
            if (curSp <= 0 || skill.getCurrentLevel() != 0) {
                log.error(String.format("Character %d attempted assigning hyper skill SP without having it, or too much. Available SP %d, current %d (%d, job %d)",
                        chr.getId(), curSp, skill.getCurrentLevel(), skillID, chr.getJob()));
                chr.dispose();
                return;
            }
            spSet.addSp(-1);
        } else if (si.getHyperStat() != 0) {
            int totalHyperSp = SkillConstants.getTotalHyperStatSpByLevel(chr.getLevel());
            int spentSp = chr.getSpentHyperSp();
            int availableSp = totalHyperSp - spentSp;
            int neededSp = SkillConstants.getNeededSpForHyperStatSkill(skill.getCurrentLevel() + 1);
            if (skill.getCurrentLevel() >= skill.getMaxLevel() || availableSp < neededSp) {
                log.error(String.format("Character %d attempted assigning too many hyper stat levels. Available SP %d, needed %d, current %d (%d, job %d)",
                        chr.getId(), availableSp, neededSp, skill.getCurrentLevel(), skillID, chr.getJob()));
                chr.dispose();
                return;
            }
        } else { // not hyper stat and not hyper skill
            log.error(String.format("Character %d attempted assigning hyper stat to an improper skill. (%d, job %d)", chr.getId(), skillID, chr.getJob()));
            chr.dispose();
            return;
        }
        chr.removeFromBaseStatCache(skill);
        skill.setCurrentLevel(skill.getCurrentLevel() + 1);
        chr.addToBaseStatCache(skill);
        List<Skill> skills = new ArrayList<>();
        skills.add(skill);
        chr.write(WvsContext.changeSkillRecordResult(skills, true, false, false, false));
    }

    public static void handleUserHyperStatSkillResetRequest(Char chr, InPacket inPacket) {
        inPacket.decodeInt(); // tick
        if (chr.getMoney() < GameConstants.HYPER_STAT_RESET_COST) {
            chr.chatMessage("Not enough money for this operation.");
            chr.write(WvsContext.receiveHyperStatSkillResetResult(chr.getId(), true, false));
        } else {
            chr.deductMoney(GameConstants.HYPER_STAT_RESET_COST);
            List<Skill> skills = new ArrayList<>();
            for (int skillID = 80000400; skillID <= 80000418; skillID++) {
                Skill skill = chr.getSkill(skillID);
                if (skill != null) {
                    skill.setCurrentLevel(0);
                    skills.add(skill);
                }
            }
            chr.write(WvsContext.changeSkillRecordResult(skills, true, false, false, false));
            chr.write(WvsContext.receiveHyperStatSkillResetResult(chr.getId(), true, true));
        }
    }

    public static void handleMobSelfDestruct(Char chr, InPacket inPacket) {
        int mobID = inPacket.decodeInt();
        Field field = chr.getField();
        Mob mob = (Mob) field.getLifeByObjectID(mobID);
        if (mob != null && mob.isSelfDestruction()) {
            field.removeLife(mobID);
            field.broadcastPacket(MobPool.leaveField(mobID, DeathType.ANIMATION_DEATH));
        }
    }

    public static void handleMobAreaAttackDisease(Char chr, InPacket inPacket) {
        int mobID = inPacket.decodeInt();
        int attackIdx = inPacket.decodeInt();
        Position areaPos = inPacket.decodePositionInt();
        int nextTickPossible = inPacket.decodeInt();
    }

    public static void handlePetMove(Char chr, InPacket inPacket) {
        int petID = inPacket.decodeInt();
        inPacket.decodeByte(); // ?
        MovementInfo movementInfo = new MovementInfo(inPacket);
        Pet pet = chr.getPetByIdx(petID);
        if (pet != null) {
            movementInfo.applyTo(pet);
            chr.getField().broadcastPacket(UserLocal.petMove(chr.getId(), petID, movementInfo), chr);
        }
    }

    public static void handlePetDropPickUpRequest(Char chr, InPacket inPacket) {
        int petID = inPacket.decodeInt();
        byte fieldKey = inPacket.decodeByte();
        inPacket.decodeInt(); // tick
        Position pos = inPacket.decodePosition();
        int dropID = inPacket.decodeInt();
        inPacket.decodeInt(); // cliCrc
        Field field = chr.getField();
        Life life = field.getLifeByObjectID(dropID);
        if (life instanceof Drop) {
            Drop drop = (Drop) life;
            boolean success = drop.canBePickedUpByPet() && drop.canBePickedUpBy(chr) && chr.addDrop(drop);
            if (success) {
                field.removeDrop(dropID, chr.getId(), false, petID);
            }
        }
    }

    public static void handleUserContentsMapRequest(Char chr, InPacket inPacket) {
        inPacket.decodeShort();
        int fieldID = inPacket.decodeInt();
        Field field = chr.getOrCreateFieldByCurrentInstanceType(fieldID);
        chr.warp(field);
    }

    public static void handleUserMapTransferRequest(Char chr, InPacket inPacket) {
        byte mtType = inPacket.decodeByte();
        byte itemType = inPacket.decodeByte();


        MapTransferType mapTransferType = MapTransferType.getByVal(mtType);
        switch (mapTransferType) {
            case DeleteListRecv: //Delete request that's received
                int targetFieldID = inPacket.decodeInt();
                HyperTPRock.removeFieldId(chr, targetFieldID);
                chr.write(WvsContext.mapTransferResult(MapTransferType.DeleteListSend, itemType, chr.getHyperRockFields()));
                break;

            case RegisterListRecv: //Register request that's received
                targetFieldID = chr.getFieldID();
                HyperTPRock.addFieldId(chr, targetFieldID);
                chr.write(WvsContext.mapTransferResult(MapTransferType.RegisterListSend, itemType, chr.getHyperRockFields()));
                break;


        }
    }

    public static void handleMonsterCollectionExploreReq(Char chr, InPacket inPacket) {
        int region = inPacket.decodeInt();
        int session = inPacket.decodeInt();
        int group = inPacket.decodeInt();
        int key = region * 10000 + session * 100 + group;
        Account account = chr.getAccount();
        MonsterCollection mc = account.getMonsterCollection();
        MonsterCollectionExploration mce = mc.getExploration(region, session, group);
        boolean complete = mc.isComplete(region, session, group);
        if (complete && mce == null) {
            // starting an exploration
            if (mc.getOpenExplorationSlots() <= 0) {
                chr.write(WvsContext.monsterCollectionResult(MonsterCollectionResultType.NotEnoughExplorationSlots, null, 0));
                return;
            }
            mce = mc.createExploration(region, session, group);
            mc.addExploration(mce);
            chr.write(UserLocal.collectionRecordMessage(mce.getPosition(), mce.getValue(true)));
            chr.write(WvsContext.monsterCollectionResult(MonsterCollectionResultType.ExploreBegin, null, 0));
        } else {
            // trying to start an incomplete/already exploring group
            chr.write(WvsContext.monsterCollectionResult(MonsterCollectionResultType.NoMonstersForExploring, null, 0));
        }
        chr.dispose(); // still required even if you send a collection result
    }

    public static void handleMonsterCollectionCompleteRewardReq(Char chr, InPacket inPacket) {
        int reqType = inPacket.decodeInt(); // 0 = group
        int region = inPacket.decodeInt();
        int session = inPacket.decodeInt();
        int group = inPacket.decodeInt();
        int exploreIndex = inPacket.decodeInt();
        MonsterCollection mc = chr.getAccount().getMonsterCollection();
        switch (reqType) {
            case 0: // group
                MonsterCollectionGroup mcs = mc.getGroup(region, session, group);
                if (mcs != null && !mcs.isRewardClaimed() && mc.isComplete(region, session, group)) {
                    Tuple<Integer, Integer> rewardInfo = MonsterCollectionData.getReward(region, session, group);
                    Item item = ItemData.getItemDeepCopy(rewardInfo.getLeft());
                    item.setQuantity(rewardInfo.getRight());
                    chr.addItemToInventory(item);
                    mcs.setRewardClaimed(true);
                    chr.write(WvsContext.monsterCollectionResult(MonsterCollectionResultType.CollectionCompletionRewardSuccess, null, 0));
                } else if (mcs != null && mcs.isRewardClaimed()) {
                    chr.write(WvsContext.monsterCollectionResult(MonsterCollectionResultType.AlreadyClaimedReward, null, 0));
                } else {
                    chr.write(WvsContext.monsterCollectionResult(MonsterCollectionResultType.CompleteCollectionBeforeClaim, null, 0));
                }
                break;
            case 4: // exploration
                MonsterCollectionExploration mce = mc.getExploration(region, session, group);
                if (mce != null && mce.getEndDate().isExpired()) {
                    mc.removeExploration(mce);
                    chr.write(UserLocal.collectionRecordMessage(mce.getPosition(), mce.getValue(false)));
                    chr.write(WvsContext.monsterCollectionResult(MonsterCollectionResultType.CollectionCompletionRewardSuccess, null, 0));
                } else {
                    chr.write(WvsContext.monsterCollectionResult(MonsterCollectionResultType.TryAgainInAMoment, null, 0));
                }
                break;
            default:
                log.warn("Unhandled MonsterCollectionCompleteRewardReq type " + reqType);
                chr.write(WvsContext.monsterCollectionResult(MonsterCollectionResultType.TryAgainInAMoment, null, 0));

        }
        chr.dispose(); // still required even if you send a collection result
    }

    public static void handleGroupMessage(Char chr, InPacket inPacket) {
        byte type = inPacket.decodeByte(); // party = 1, alliance = 3
        byte idk2 = inPacket.decodeByte();
        int idk3 = inPacket.decodeInt(); // party id?
        String msg = inPacket.decodeString();
        if (type == 1 && chr.getParty() != null) {
            chr.getParty().broadcast(CField.groupMessage(GroupMessageType.Party, chr.getName(), msg), chr);
        } else if (type == 3 && chr.getGuild() != null && chr.getGuild().getAlliance() != null) {
            chr.getGuild().getAlliance().broadcast(CField.groupMessage(GroupMessageType.Alliance, chr.getName(), msg), chr);
        }
    }

    public static void handleUserFieldTransferRequest(Char chr, InPacket inPacket) {
        int fieldID = inPacket.decodeInt();
        if (fieldID == 7860) {
            Field field = chr.getOrCreateFieldByCurrentInstanceType(910001000);
            chr.warp(field);
        }
    }

    public static void handleEnterOpenGateRequest(Char chr, InPacket inPacket) {
        int chrId = inPacket.decodeInt();
        Position position = inPacket.decodePosition();
        byte gateId = inPacket.decodeByte();

        // Probably needs remote player position handling

        chr.dispose(); // Necessary as going through the portal will stuck you
    }

    public static void handleGoldHammerRequest(Char chr, InPacket inPacket) {
        if (chr.getClient().getWorld().isReboot()) {
            log.error(String.format("Character %d attempted to hammer in reboot world.", chr.getId()));
            chr.dispose();
            return;
        }

        final int delay = 2700; // 2.7 sec delay to match golden hammer's animation

        inPacket.decodeInt(); // tick
        int iPos = inPacket.decodeInt(); // hammer slot
        inPacket.decodeInt(); // hammer item id
        inPacket.decodeInt(); // use hammer? useless though
        int ePos = inPacket.decodeInt(); // equip slot

        EventManager.addEvent(() -> {
            Equip equip = (Equip) chr.getInventoryByType(EQUIP).getItemBySlot((short) ePos);
            Item hammer = chr.getInventoryByType(CONSUME).getItemBySlot((short) iPos);

            if (equip == null || !ItemConstants.canEquipGoldHammer(equip) ||
                    hammer == null || !ItemConstants.isGoldHammer(hammer)) {
                log.error(String.format("Character %d tried to use hammer (id %d) on an invalid equip (id %d)",
                        chr.getId(), hammer == null ? 0 : hammer.getItemId(), equip == null ? 0 : equip.getItemId()));
                chr.write(WvsContext.goldHammerItemUpgradeResult((byte) 3, 1, 0));
                return;
            }

            Map<ScrollStat, Integer> vals = ItemData.getItemInfoByID(hammer.getItemId()).getScrollStats();

            if (vals.size() > 0) {
                if (equip.getBaseStat(iuc) >= ItemConstants.MAX_HAMMER_SLOTS) {
                    log.error(String.format("Character %d tried to use hammer (id %d) an invalid equip (%d/%d)",
                            chr.getId(), equip == null ? 0 : equip.getItemId()));
                    chr.write(WvsContext.goldHammerItemUpgradeResult((byte) 3, 2, 0));
                    return;
                }

                boolean success = Util.succeedProp(vals.getOrDefault(ScrollStat.success, 100));

                if (success) {
                    equip.addStat(iuc, 1); // +1 hammer used
                    equip.addStat(tuc, 1); // +1 upgrades available
                    equip.updateToChar(chr);
                    chr.chatMessage(String.format("Successfully expanded upgrade slots. (%d/%d)", equip.getIuc(), ItemConstants.MAX_HAMMER_SLOTS));
                    chr.write(WvsContext.goldHammerItemUpgradeResult((byte) 2, 0, ItemConstants.MAX_HAMMER_SLOTS - (int) equip.getBaseStat(iuc)));
                } else {
                    chr.chatMessage(String.format("Failed to expand upgrade slots. (%d/%d)", equip.getIuc(), ItemConstants.MAX_HAMMER_SLOTS));
                    chr.write(WvsContext.goldHammerItemUpgradeResult((byte) 2, 1, 0));
                }

                chr.consumeItem(hammer.getItemId(), 1);
            }
        }, delay);
    }

    public static void handleGoldHammerComplete(Char chr, InPacket inPacket) {
        chr.dispose();
    }

    public static void handleUserRequestInstanceTable(Char chr, InPacket inPacket) {
        String requestStr = inPacket.decodeString();
        int type = inPacket.decodeInt();
        int subType = inPacket.decodeInt();

        InstanceTableType itt = InstanceTableType.getByStr(requestStr);
        if (itt == null) {
            log.error(String.format("Unknown instance table type request %s, type %d, subType %d", requestStr, type, subType));
            return;
        }
        int value;
        switch (itt) {
            case HyperActiveSkill:
            case HyperPassiveSkill:
                ExtendSP esp = chr.getAvatarData().getCharacterStat().getExtendSP();
                if (subType == InstanceTableType.HyperPassiveSkill.getSubType()) {
                    value = esp.getSpByJobLevel(SkillConstants.PASSIVE_HYPER_JOB_LEVEL);
                } else {
                    value = esp.getSpByJobLevel(SkillConstants.ACTIVE_HYPER_JOB_LEVEL);
                }
                break;
            case HyperStatIncAmount:
                // type == level
                value = SkillConstants.getHyperStatSpByLv((short) type);
                break;
            case NeedHyperStatLv:
                // type == skill lv
                value = SkillConstants.getNeededSpForHyperStatSkill(type);
                break;
            default:
                log.error(String.format("Unhandled instance table type request %s, type %d, subType %d", itt, type, subType));
                return;
        }

        chr.write(WvsContext.resultInstanceTable(requestStr, type, subType, true, value));
    }

    public static void handleMakeEnterFieldPacketForQuickMove(Char chr, InPacket inPacket) {
        int templateID = inPacket.decodeInt();
        Npc npc = NpcData.getNpcDeepCopyById(templateID);
        String script = npc.getScripts().get(0);
        if (script == null) {
            script = String.valueOf(npc.getTemplateId());
        }
        chr.getScriptManager().startScript(npc.getTemplateId(), templateID, script, ScriptType.Npc);

    }

    public static void handleUserDefaultWingItem(Char chr, InPacket inPacket) {
        int wingItem = inPacket.decodeInt();
        if (wingItem == 5010093) { // AB
            chr.getAvatarData().getCharacterStat().setWingItem(wingItem);
            chr.getField().broadcastPacket(UserRemote.setDefaultWingItem(chr));
        }
    }

    public static void handleUserSetDressChangedRequest(Char chr, InPacket inPacket) {
        boolean on = inPacket.decodeByte() != 0;
//        chr.write(UserLocal.setDressChanged(on, true)); // causes client to send this packet again
    }

    public static void handleEnterTownPortalRequest(Char chr, InPacket inPacket) {
        int chrId = inPacket.decodeInt(); // Char id
        boolean town = inPacket.decodeByte() != 0;

        Field field = chr.getField();
        TownPortal townPortal = field.getTownPortalByChrId(chrId);
        if (townPortal != null) {       // TODO Using teleports, as grabbing the TownPortalPoint portal id is not working
            if (town) {
                // townField -> fieldField
                Field fieldField = townPortal.getChannel().getField(townPortal.getFieldFieldId());

                chr.warp(fieldField); // Back to the original Door
                chr.write(CField.teleport(townPortal.getFieldPosition(), chr)); // Teleports player to the position of the TownPortal
            } else {
                // fieldField -> townField
                Field returnField = townPortal.getChannel().getField(townPortal.getTownFieldId()); // Initialise the Town Map,

                chr.warp(returnField); // warp Char
                chr.write(CField.teleport(townPortal.getTownPosition(), chr));
                if (returnField.getTownPortalByChrId(chrId) == null) { // So that every re-enter into the TownField doesn't spawn another TownPortal
                    returnField.broadcastPacket(WvsContext.townPortal(townPortal)); // create the TownPortal
                    returnField.addTownPortal(townPortal);
                }
            }
        } else {
            chr.dispose();
            log.warn("Character {" + chrId + "} tried entering a Town Portal in field {" + field.getId() + "} which does not exist."); // Potential Hacking Log
        }
    }

    public static void handleMiniRoom(Char chr, InPacket inPacket) {
        if (chr.getClient().getWorld().isReboot()) {
            log.error(String.format("Character %d attempted to use trade in reboot world.", chr.getId()));
            chr.dispose();
            return;
        }
        chr.dispose();
        byte type = inPacket.decodeByte(); // MiniRoom Type value
        MiniRoomType mrt = MiniRoomType.getByVal(type);
        if (mrt == null) {
            log.error(String.format("Unknown miniroom type %d", type));
            return;
        }
        TradeRoom tradeRoom = chr.getTradeRoom();
        switch (mrt) {
            case PlaceItem:
            case PlaceItem_2:
            case PlaceItem_3:
            case PlaceItem_4:
                byte invType = inPacket.decodeByte();
                short bagIndex = inPacket.decodeShort();
                short quantity = inPacket.decodeShort();
                byte tradeSlot = inPacket.decodeByte(); // trade window slot number

                Item item = chr.getInventoryByType(InvType.getInvTypeByVal(invType)).getItemBySlot(bagIndex);
                if (item.getQuantity() < quantity) {
                    log.warn(String.format("Character {%d} tried to trade an item {%d} with a higher quantity {%s} than the item has {%d}.", chr.getId(), item.getItemId(), quantity, item.getQuantity()));
                    return;
                }
                if (!item.isTradable()) {
                    log.warn(String.format("Character {%d} tried to trade an item {%d} whilst it was trade blocked.", chr.getId(), item.getItemId()));
                    return;
                }
                if (chr.getTradeRoom() == null) {
                    chr.chatMessage("You are currently not trading.");
                    return;
                }
                Item offer = ItemData.getItemDeepCopy(item.getItemId());
                offer.setQuantity(quantity);
                if (tradeRoom.canAddItem(chr)) {
                    int consumed = quantity > item.getQuantity() ? 0 : item.getQuantity() - quantity;
                    item.setQuantity(consumed + 1); // +1 because 1 gets consumed by consumeItem(item)
                    chr.consumeItem(item);
                    tradeRoom.addItem(chr, tradeSlot, offer);
                }
                Char other = tradeRoom.getOtherChar(chr);
                chr.write(MiniroomPacket.putItem(0, tradeSlot, offer));
                other.write(MiniroomPacket.putItem(1, tradeSlot, offer));

                break;
            case SetMesos:
            case SetMesos_2:
            case SetMesos_3:
            case SetMesos_4:
                long money = inPacket.decodeLong();
                if (tradeRoom == null) {
                    chr.chatMessage("You are currently not trading.");
                    return;
                }
                if (money < 0 || money > chr.getMoney()) {
                    log.error(String.format("Character %d tried to add an invalid amount of mesos(%d, own money: %d)",
                            chr.getId(), money, chr.getMoney()));
                    return;
                }
                chr.deductMoney(money);
                chr.addMoney(tradeRoom.getMoney(chr));
                tradeRoom.putMoney(chr, money);
                other = tradeRoom.getOtherChar(chr);
                chr.write(MiniroomPacket.putMoney(0, money));
                other.write(MiniroomPacket.putMoney(1, money));
                break;
            case Trade:
            case TradeConfirm:
            case TradeConfirm2:
            case TradeConfirm3:
                other = tradeRoom.getOtherChar(chr);
                other.write(MiniroomPacket.tradeConfirm());
                if (tradeRoom.hasConfirmed(other)) {
                    boolean success = tradeRoom.completeTrade();
                    if (success) {
                        chr.write(MiniroomPacket.tradeComplete());
                        other.write(MiniroomPacket.tradeComplete());
                    } else {
                        tradeRoom.cancelTrade();
                        tradeRoom.getChr().write(MiniroomPacket.cancelTrade());
                        tradeRoom.getOther().write(MiniroomPacket.cancelTrade());
                    }
                    chr.setTradeRoom(null);
                    other.setTradeRoom(null);
                } else {
                    tradeRoom.addConfirmedPlayer(chr);
                }
                break;
            case Chat:
                inPacket.decodeInt(); // tick
                String msg = inPacket.decodeString();
                if (tradeRoom == null) {
                    chr.chatMessage("You are currently not in a room.");
                    return;
                }
                // this is kinda weird atm, so no different colours
                String msgWithName = String.format("%s : %s", chr.getName(), msg);
                chr.write(MiniroomPacket.chat(1, msgWithName));
                tradeRoom.getOtherChar(chr).write(MiniroomPacket.chat(0, msgWithName));
                break;
            case Accept:
                if (tradeRoom == null) {
                    chr.chatMessage("Your trade partner cancelled the trade.");
                    return;
                }
                chr.write(MiniroomPacket.enterTrade(tradeRoom, chr));
                other = tradeRoom.getOtherChar(chr); // initiator
                other.write(MiniroomPacket.enterTrade(tradeRoom, other));

                // Start Custom ----------------------------------------------------------------------------------------
                String[] inventoryNames = new String[]{
                        "eqp",
                        "use",
                        "etc",
                        "setup",
                        "cash",
                };
                for (String invName : inventoryNames) {
                    chr.write(MiniroomPacket.chat(1, String.format("%s has %d free %s slots", other.getName(), other.getInventoryByType(InvType.getInvTypeByString(invName)).getEmptySlots(), invName)));
                    other.write(MiniroomPacket.chat(1, String.format("%s has %d free %s slots", chr.getName(), chr.getInventoryByType(InvType.getInvTypeByString(invName)).getEmptySlots(), invName)));
                }
                // End Custom ------------------------------------------------------------------------------------------

                break;
            case TradeInviteRequest:
                int charID = inPacket.decodeInt();
                other = chr.getField().getCharByID(charID);
                if (other == null) {
                    chr.chatMessage("Could not find that player.");
                    return;
                }
                if (other.getTradeRoom() != null) {
                    chr.chatMessage("That player is already trading.");
                    return;
                }
                other.write(MiniroomPacket.tradeInvite(chr));
                tradeRoom = new TradeRoom(chr, other);
                chr.setTradeRoom(tradeRoom);
                other.setTradeRoom(tradeRoom);
                break;
            case InviteResultStatic: // always decline?
                if (tradeRoom != null) {
                    other = tradeRoom.getOtherChar(chr);
                    other.chatMessage(String.format("%s has declined your trade invite.", chr.getName()));
                    other.setTradeRoom(null);
                }
                chr.setTradeRoom(null);
                break;
            case ExitTrade:
                if (tradeRoom != null) {
                    tradeRoom.cancelTrade();
                    tradeRoom.getOtherChar(chr).write(MiniroomPacket.cancelTrade());
                }
                break;
            case TradeConfirmRemoteResponse:
                // just an ack by the client?
                break;
            default:
                log.error(String.format("Unhandled miniroom type %s", mrt));
        }
    }

    public static void handleUserEffectLocal(Char chr, InPacket inPacket) {
        int skillId = inPacket.decodeInt();
        byte slv = inPacket.decodeByte();
        byte sendLocal = inPacket.decodeByte();

        int chrId = chr.getId();
        Field field = chr.getField();

        if (!chr.hasSkill(skillId)) {
            log.warn(String.format("Character {%d} tried to use a skill {%d} they do not have.", chrId, skillId));
        }


        if (skillId == Evan.DRAGON_FURY) {
            field.broadcastPacket(UserRemote.effect(chrId, Effect.showDragonFuryEffect(skillId, slv, 0, true)));

        } else if (skillId == Warrior.FINAL_PACT) {
            field.broadcastPacket(UserRemote.effect(chrId, Effect.showFinalPactEffect(skillId, slv, 0, true)));

        } else if (skillId == WildHunter.CALL_OF_THE_HUNTER) {
            field.broadcastPacket(UserRemote.effect(chrId, Effect.showCallOfTheHunterEffect(skillId, slv, 0, chr.isLeft(), chr.getPosition().getX(), chr.getPosition().getY())));

        } else if (skillId == Kaiser.VERTICAL_GRAPPLE || skillId == AngelicBuster.GRAPPLING_HEART) { // 'Grappling Hook' Skills
            int chrPositionY = inPacket.decodeInt();
            Position ropeConnectDest = inPacket.decodePositionInt();
            field.broadcastPacket(UserRemote.effect(chrId, Effect.showVerticalGrappleEffect(skillId, slv, 0, chrPositionY, ropeConnectDest.getX(), ropeConnectDest.getY())));

        } else if (skillId == 15001021/*TB  Flash*/ || skillId == Shade.FOX_TROT) { // 'Flash' Skills
            Position origin = inPacket.decodePositionInt();
            Position dest = inPacket.decodePositionInt();
            field.broadcastPacket(UserRemote.effect(chrId, Effect.showFlashBlinkEffect(skillId, slv, 0, origin.getX(), origin.getY(), dest.getX(), dest.getY())));

        } else if (SkillConstants.isSuperNovaSkill(skillId)) { // 'SuperNova' Skills
            Position chrPosition = inPacket.decodePositionInt();
            field.broadcastPacket(UserRemote.effect(chrId, Effect.showSuperNovaEffect(skillId, slv, 0, chrPosition.getX(), chrPosition.getY())));

        } else if (SkillConstants.isUnregisteredSkill(skillId)) { // 'Unregistered' Skills
            field.broadcastPacket(UserRemote.effect(chrId, Effect.showUnregisteredSkill(skillId, slv, 0, chr.isLeft())));

        } else if (SkillConstants.isHomeTeleportSkill(skillId)) {
            field.broadcastPacket(UserRemote.effect(chrId, Effect.skillUse(skillId, slv, 0)));

        } else if (skillId == BattleMage.DARK_SHOCK) {
            Position origin = inPacket.decodePositionInt();
            Position dest = inPacket.decodePositionInt();
            field.broadcastPacket(UserRemote.effect(chrId, Effect.showDarkShockSkill(skillId, slv, origin, dest)));
        } else {
            log.error(String.format("Unhandled Remote Effect Skill id %d", skillId));
            chr.chatMessage(String.format("Unhandled Remote Effect Skill:  id = %d", skillId));
        }
    }

    public static void handleGuildBBS(Char chr, InPacket inPacket) {
        Guild guild = chr.getGuild();
        if (guild == null) {
            return;
        }
        byte val = inPacket.decodeByte();
        GuildBBSType type = GuildBBSType.getByValue(val);
        if (type == null) {
            log.error(String.format("Unknown guild bbs type %s", val));
            return;
        }
        switch (type) {
            case Req_CreateRecord:
                inPacket.decodeByte(); // 0?
                boolean notice = inPacket.decodeByte() != 0;
                String subject = inPacket.decodeString();
                String msg = inPacket.decodeString();
                int icon = inPacket.decodeInt();
                BBSRecord record = new BBSRecord(chr.getId(), subject, msg, FileTime.currentTime(), icon);
                if (notice) {
                    guild.setBbsNotice(record);
                } else {
                    guild.addBbsRecord(record);
                }
                chr.write(WvsContext.guildBBSResult(GuildBBSPacket.loadRecord(record)));
                break;
            case Req_DeleteRecord:
                int id = inPacket.decodeInt();
                record = guild.getRecordByID(id);
                if (record != null && chr.getId() == record.getCreatorID()) {
                    guild.removeRecord(record);
                }
                break;
            case Req_LoadPages:
                int page = inPacket.decodeInt();
                List<BBSRecord> records = guild.getBbsRecords()
                        .stream()
                        .sorted(Comparator.comparingInt(BBSRecord::getIdForBbs))
                        .collect(Collectors.toList());
                final int PAGE_SIZE = GameConstants.GUILD_BBS_RECORDS_PER_PAGE;
                if (page != 0 && page * PAGE_SIZE >= records.size()) {
                    chr.chatMessage("No more BBS records to show.");
                    return;
                }
                // select all records that are on the requested page
                int start = page * PAGE_SIZE;
                int end = start + PAGE_SIZE >= records.size() ? records.size() : start + PAGE_SIZE;
                List<BBSRecord> pageRecords = records.subList(start, end);
                chr.write(WvsContext.guildBBSResult(GuildBBSPacket.loadPages(guild.getBbsNotice(), records.size(), pageRecords)));
                break;
            case Req_LoadRecord:
                id = inPacket.decodeInt();
                record = guild.getRecordByID(id);
                chr.write(WvsContext.guildBBSResult(GuildBBSPacket.loadRecord(record)));
                break;
            case Req_CreateReply:
                id = inPacket.decodeInt();
                msg = inPacket.decodeString();
                record = guild.getRecordByID(id);
                if (record != null) {
                    BBSReply reply = new BBSReply(chr.getId(), FileTime.currentTime(), msg);
                    record.addReply(reply);
                }
                chr.write(WvsContext.guildBBSResult(GuildBBSPacket.loadRecord(record)));
                break;
            case Req_DeleteReply:
                id = inPacket.decodeInt();
                int replyId = inPacket.decodeInt();
                record = guild.getRecordByID(id);
                if (record != null) {
                    BBSReply reply = record.getReplyById(replyId);
                    record.removeReply(reply);
                }
                chr.write(WvsContext.guildBBSResult(GuildBBSPacket.loadRecord(record)));
            default:
                log.error(String.format("Unhandled guild bbs type %s", type));

        }
    }

    public static void handleBeastTamerRegroupRequest(Char chr, InPacket inPacket) {
        byte unk = inPacket.decodeByte();
        int skillId = inPacket.decodeInt();

        if (skillId == BeastTamer.REGROUP) {
            BeastTamer.beastTamerRegroup(chr);
        } else {
            log.error(String.format("Unhandled Beast Tamer Request %d", skillId));
            chr.chatMessage(String.format("Unhandled Beast Tamer Request %d", skillId));
        }
    }

    public static void handleUserJaguarChangeRequest(Char chr, InPacket inPacket) {
        final int questID = QuestConstants.WILD_HUNTER_JAGUAR_STORAGE_ID;
        Quest quest = chr.getQuestManager().getQuestById(questID);
        if (quest == null) {
            return;
        }
        quest.convertQRValueToProperties();
        int fromID = inPacket.decodeInt();
        int toID = inPacket.decodeInt();
        String value = quest.getProperty("" + (toID + 1));
        if (value != null) {
            WildHunterInfo whi = chr.getWildHunterInfo();
            whi.setIdx((byte) toID);
            whi.setRidingType((byte) toID);
            chr.write(WvsContext.wildHunterInfo(whi));
        } else {
            chr.chatMessage("You do not have that jaguar.");
        }
    }

    public static void handleB2BodyRequest(Char chr, InPacket inPacket) {
        short type = inPacket.decodeShort();
        int ownerCID = inPacket.decodeInt();
        int bodyIdCounter = inPacket.decodeInt();
        Position offsetPos = inPacket.decodePosition();
        int skillID = inPacket.decodeInt();
        boolean isLeft = inPacket.decodeByte() != 0;
        inPacket.decodeByte();
        inPacket.decodeShort();
        inPacket.decodeShort();
        inPacket.decodeShort();
        Position forcedPos = inPacket.decodePositionInt();
    }

    public static void handleUserRegisterPetAutoBuffRequest(Char chr, InPacket inPacket) {
        int petIdx = inPacket.decodeInt();
        int skillID = inPacket.decodeInt();
        SkillInfo si = SkillData.getSkillInfoById(skillID);
        Skill skill = chr.getSkill(skillID);
        Pet pet = chr.getPetByIdx(petIdx);
        int coolTime = si == null ? 0 : si.getValue(SkillStat.cooltime, 1);
        if (skillID != 0 && (si == null || pet == null || !pet.getItem().hasPetSkill(PetSkill.AUTO_BUFF) ||
                skill == null || skill.getCurrentLevel() == 0 || coolTime > 0)) {
            chr.chatMessage("Something went wrong when adding the pet skill.");
            log.error(String.format("Character %d tried to illegally add a pet skill (skillID = %d, skill = %s, " +
                    "pet = %s, coolTime = %d)", chr.getId(), skillID, skill, pet, coolTime));
            chr.dispose();
            return;
        }
        pet.getItem().setAutoBuffSkill(skillID);
        pet.getItem().updateToChar(chr);
    }

    public static void handleUserGivePopularityRequest(Char chr, InPacket inPacket) {
        int targetChrId = inPacket.decodeInt();
        boolean increase = inPacket.decodeByte() != 0;

        Field field = chr.getField();
        Char targetChr = field.getCharByID(targetChrId);
        CharacterStat cs = chr.getAvatarData().getCharacterStat();

        if (targetChr == null) { // Faming someone who isn't in the map or doesn't exist
            chr.write(WvsContext.givePopularityResult(PopularityResultType.InvalidCharacterId, targetChr, 0, increase));
            chr.dispose();
        } else if (chr.getLevel() < GameConstants.MIN_LEVEL_TO_FAME || targetChr.getLevel() < GameConstants.MIN_LEVEL_TO_FAME) { // Chr or TargetChr is too low level
            chr.write(WvsContext.givePopularityResult(PopularityResultType.LevelLow, targetChr, 0, increase));
            chr.dispose();
        } else if (!cs.getNextAvailableFameTime().isExpired()) { // Faming whilst Chr already famed within the FameCooldown time
            chr.write(WvsContext.givePopularityResult(PopularityResultType.AlreadyDoneToday, targetChr, 0, increase));
            chr.dispose();
        } else if (targetChrId == chr.getId()) {
            log.error(String.format("Character %d tried to fame themselves", chr.getId()));
        } else {
            targetChr.addStatAndSendPacket(pop, (increase ? 1 : -1));
            int curPop = targetChr.getAvatarData().getCharacterStat().getPop();
            chr.write(WvsContext.givePopularityResult(PopularityResultType.Success, targetChr, curPop, increase));
            targetChr.write(WvsContext.givePopularityResult(PopularityResultType.Notify, chr, curPop, increase));
            cs.setNextAvailableFameTime(FileTime.fromDate(LocalDateTime.now().plusHours(GameConstants.FAME_COOLDOWN)));
            if (increase) {
                Effect.showFameGradeUp(targetChr);
            }
        }
    }

    public static void handleEnterRandomPortalRequest(Char chr, InPacket inPacket) {
        int portalObjID = inPacket.decodeInt();
        Life life = chr.getField().getLifeByObjectID(portalObjID);
        if (!(life instanceof RandomPortal)) {
            chr.chatMessage("Could not find that portal.");
            return;
        }
        RandomPortal randomPortal = (RandomPortal) life;
        if (randomPortal.getCharID() != chr.getId()) {
            chr.chatMessage("A mysterious force is blocking your way.");
            chr.dispose();
            return;
        }
        RandomPortal.Type type = randomPortal.getAppearType();
        String script = type.getScript();
        chr.getScriptManager().startScript(randomPortal.getAppearType().ordinal(), randomPortal.getObjectId(),
                script, ScriptType.Portal);
        chr.dispose();
    }

    public static void handleLibraryStartScript(Char chr, InPacket inPacket) {
        int bookId = inPacket.decodeByte();
        if (chr.getQuestManager().hasQuestCompleted(32662)) {
            int questID = QuestConstants.DIMENSION_LIBRARY + bookId;
            chr.getScriptManager().startScript(questID, "q" + Integer.toString(questID) + "s", ScriptType.Quest);
        }
    }

    public static void handleMobRequestEscortInfo(Char chr, InPacket inPacket) {
        Field field = chr.getField();
        int objectID = inPacket.decodeInt();
        Life life = field.getLifeByObjectID(objectID);
        if (!(life instanceof Mob)) {
            return;
        }
        Mob mob = (Mob) life;
        if (mob.isEscortMob()) {

            // [Grand Athenaeum] Ariant : Escort Hatsar's Servant
            if (mob.getTemplateId() == 8230000) {
                mob.addEscortDest(-1616, 233, -1);
                mob.addEscortDest(1898, 233, 0);
                mob.escortFullPath(-1);
                chr.write(CField.removeBlowWeather());
                chr.write(CField.blowWeather(5120118, "I'm glad you're here, " + chr.getName() + "! Please get rid of these pesky things."));
            }
        }
    }

    public static void handleMobEscortCollision(Char chr, InPacket inPacket) {
        Field field = chr.getField();
        int objectID = inPacket.decodeInt();
        Life life = field.getLifeByObjectID(objectID);
        if (!(life instanceof Mob)) {
            return;
        }
        Mob mob = (Mob) life;
        int collision = inPacket.decodeInt();

        EscortDest escortDest = mob.getEscortDest().get(collision - 1);
        if (escortDest != null) {
            // mob movement don't updating mob position so I disabled it until it will.
            /*if (escortDest.getDestPos().getX() != mob.getPosition().getX() || escortDest.getDestPos().getY() != mob.getPosition().getY()) {
                return;
            }*/

            // [Grand Athenaeum] Ariant : Escort Hatsar's Servant
            if (mob.getTemplateId() == 8230000) {
                if (collision == 1) {
                    chr.write(CField.removeBlowWeather());
                    chr.write(CField.blowWeather(5120118, "I'm glad you're here, " + chr.getName() + "! Please get rid of these pesky things."));
                } else if (collision == 2) {
                    chr.write(CField.fieldEffect(FieldEffect.getFieldEffectFromWz("quest/party/clear", 0)));
                    chr.write(CField.fieldEffect(FieldEffect.playSound("Party1/Clear", 100)));
                    chr.write(CField.removeBlowWeather());
                    chr.write(CField.blowWeather(5120118, "Looks like we all arrived in one piece. Now, get out of here before those pesky things start bothering you again."));
                    Quest quest = chr.getQuestManager().getQuestById(32628);
                    if (quest == null) {
                        quest = new Quest(32628, QuestStatus.Started);
                        chr.getQuestManager().addQuest(quest);
                    }
                    quest.setProperty("guard1", "1");// needed to complete quest
                    chr.write(WvsContext.message(MessageType.QUEST_RECORD_EX_MESSAGE,
                            quest.getQRKey(), quest.getQRValue(), (byte) 0));
                }
            }
            mob.setCurrentDestIndex(collision);
            if (collision ==  mob.getEscortDest().size()) {
                mob.clearEscortDest();// finished escort
            }
        }
    }

    public static void handleCrossHunterCompleteRequest(Char chr, InPacket inPacket) {
        short tab = inPacket.decodeShort();
        HashMap<Item, Integer> itemMap = new HashMap<>();

        if (chr.getScriptManager().getQRValue(QuestConstants.SILENT_CRUSADE_WANTED_TAB_1 + tab).contains("r=1")) {
            log.error(String.format("Character %d tried to complete Silent Crusade Chapter %d, whilst already having claimed the reward.", chr.getId(), tab + 1));
            chr.dispose();
            return;
        }

        switch (tab) {
            case 0: // Chapter 1
                itemMap.put(ItemData.getItemDeepCopy(3700031), 1);  // Apprentice Hunter
                itemMap.put(ItemData.getItemDeepCopy(4310029), 10); // Crusader Coins  x10
                break;
            case 1: // Chapter 2
                itemMap.put(ItemData.getItemDeepCopy(3700032), 1);  // Capable Hunter
                itemMap.put(ItemData.getItemDeepCopy(4001832), 100);// Spell Traces  x100
                itemMap.put(ItemData.getItemDeepCopy(4310029), 15); // Crusader Coins  x15
                break;
            case 2: // Chapter 3
                itemMap.put(ItemData.getItemDeepCopy(3700033), 1);  // Veteran Hunter
                itemMap.put(ItemData.getItemDeepCopy(2430668), 1);  // Silent Crusade Mastery Book
                itemMap.put(ItemData.getItemDeepCopy(4310029), 20); // Crusader Coins  x20
                break;
            case 3: // Chapter 4
                itemMap.put(ItemData.getItemDeepCopy(3700034), 1);  // Superior Hunter
                itemMap.put(ItemData.getItemDeepCopy(4001832), 500);// Spell Traces  x500
                itemMap.put(ItemData.getItemDeepCopy(4310029), 30); // Crusader Coins  x30
                break;
        }

        if(!chr.canHold(new ArrayList<>(itemMap.keySet()))) {
            chr.chatMessage("Please make some space in your inventory.");
            chr.dispose();
            return;
        }

        chr.getScriptManager().setQRValue(QuestConstants.SILENT_CRUSADE_WANTED_TAB_1 + tab, "r=1");
        for (Map.Entry<Item, Integer> entry : itemMap.entrySet()) {
            Item item = entry.getKey();
            item.setQuantity(entry.getValue());
            chr.addItemToInventory(item);
        }
        chr.dispose();
    }

    public static void handleCrossHunterShopRequest(Char chr, InPacket inPacket) {
        short itemIndexInShop = inPacket.decodeShort();
        int itemId = inPacket.decodeInt();
        short itemQuantity = inPacket.decodeShort();

        int crusaderCoin = 4310029;
        List<Integer> coinCostList = Arrays.asList(50, 40, 60, 255, 170, 85, 170, 85, 10);
        List<Integer> itemList = Arrays.asList(1132111, 1152069, 1122157, 1012331, 1022148, 1032156, 1122208, 1132182, 2030026);

        if (!itemList.contains(itemId)) {
            log.error(String.format("Character %d tried to trade an item {%d} that is not in the shop list.", chr.getId(), itemId));

        } else if (itemList.get(itemIndexInShop) != itemId) {
            log.error(String.format("Character %d tried to trade an item {%d} that is not in the given position {%d}.", chr.getId(), itemId, itemIndexInShop));

        } else if (ItemConstants.isEquip(itemId) && itemQuantity > 1) {
            log.error(String.format("Character %d tried to get a quantity {%d} that is more than 1 Silent Crusade equip {%d}.", chr.getId(), itemQuantity, itemId));

        } else if (itemIndexInShop >= itemList.size()) {
            log.error(String.format("Character %d tried to get an item from a shopIndex {%d} that is more than or equal to the amount of items in the shop {%d}.", chr.getId(), itemIndexInShop, itemList.size()));

        } else if (!chr.hasItemCount(crusaderCoin, (coinCostList.get(itemIndexInShop)) * itemQuantity)) {
            chr.chatMessage("You don't have enough Crusader Coins.");

        } else if (!chr.canHold(itemId)) {
            chr.chatMessage("You don't have any inventory space.");

        } else {
            chr.consumeItem(crusaderCoin, coinCostList.get(itemIndexInShop));
            chr.addItemToInventory(itemList.get(itemIndexInShop), 1);
        }
        chr.dispose();
    }

    public static void handleUserMedalReissueRequest(Char chr, InPacket inPacket) {
        int questId = inPacket.decodeInt();
        int medalItemId = inPacket.decodeInt();
        ScriptManagerImpl sm = chr.getScriptManager();
        long actualMesoCost;
        int count = 0;
        if (sm.getQRValue(QuestConstants.MEDAL_REISSUE_QUEST).contains("count=")) {
            String countString = sm.getQRValue(QuestConstants.MEDAL_REISSUE_QUEST).replace("count=", "");
            count = Integer.parseInt(countString);
        } else {
            sm.createQuestWithQRValue(QuestConstants.MEDAL_REISSUE_QUEST, "");
        }
        switch (count) {
            case 0:
                actualMesoCost = 100;
                break;
            case 1:
                actualMesoCost = 1000;
                break;
            case 2:
                actualMesoCost = 10000;
                break;
            case 3:
                actualMesoCost = 100000;
                break;
            default:
                actualMesoCost = 1000000;
                break;
        }
        if (QuestData.getQuestInfoById(questId).getMedalItemId() != medalItemId || !(ItemConstants.isMedal(medalItemId))) {
            log.error(String.format("Character %d tried to reissue an item {%d} that isn't a medal or tried to reissue a medal from a quest {%d} that doesn't give the given medal", chr.getId(), medalItemId, questId));

        } else if (!sm.hasQuestCompleted(questId)) {
            log.error(String.format("Character %d tried to reissue a medal from a quest {} which they have not completed.", chr.getId(), questId));

        } else if (ItemData.getItemDeepCopy(medalItemId) == null || QuestData.getQuestInfoById(questId) == null) {
            chr.write(UserLocal.medalReissueResult(MedalReissueResultType.Unknown, medalItemId));

        } else if (chr.getMoney() < actualMesoCost) {
            chr.write(UserLocal.medalReissueResult(MedalReissueResultType.NoMoney, medalItemId));

        } else if (!chr.canHold(medalItemId)) {
            chr.write(UserLocal.medalReissueResult(MedalReissueResultType.NoSlot, medalItemId));

        } else if (chr.hasItem(medalItemId)) {
            chr.write(UserLocal.medalReissueResult(MedalReissueResultType.AlreadyHas, medalItemId));

        } else {
            count++;
            sm.setQRValue(QuestConstants.MEDAL_REISSUE_QUEST, "count=" + count);
            chr.deductMoney(actualMesoCost);
            chr.addItemToInventory(QuestData.getQuestInfoById(questId).getMedalItemId(), 1);
            chr.write(UserLocal.medalReissueResult(MedalReissueResultType.Success, medalItemId));
        }
        chr.dispose();
    }

    public static void handleUserSkillPrepareRequest(Char chr, InPacket inPacket) {
        int skillId = inPacket.decodeInt();
        int startTime = inPacket.decodeInt();
        int unknownInt = inPacket.decodeInt();

        if (!chr.hasSkill(skillId)) {
            return;
        }

        Skill skill = chr.getSkill(skillId);
        chr.getField().broadcastPacket(UserRemote.skillPrepare(chr, skillId, (byte) skill.getCurrentLevel()), chr);
    }
}