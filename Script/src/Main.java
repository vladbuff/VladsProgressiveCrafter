
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.osbot.rs07.api.Bank;
import org.osbot.rs07.api.Mouse;
import org.osbot.rs07.api.Settings;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
import org.osbot.rs07.utility.ConditionalSleep;

import javax.imageio.ImageIO;
import javax.swing.*;

@ScriptManifest(author = "Vlad's", info = "Progressive crafter", name = "Vlads Progressive Crafter", version = 0, logo = "")
public class Main extends Script {

    private RS2Widget geConfirmWidget() {
        return getWidgets().getWidgetContainingText("History");
    }

    private RS2Widget collectWidget() {
        return getWidgets().getWidgetContainingText("Collect");
    }

    private RS2Widget leatherGloveCraftConfirm() {
        List<RS2Widget> allWidgets = getWidgets().getAll();
        return allWidgets.stream().filter(w -> w.getWidth() == 62 && w.getHeight() == 62 && w.getItemId() == 1059).findFirst().orElse(null);
    }

    private RS2Widget goldNecklaceCraftConfirm() {
        List<RS2Widget> allWidgets = getWidgets().getAll();
        return allWidgets.stream().filter(w -> w.getWidth() == 32 && w.getHeight() == 32 && w.getItemId() == goldNecklaceUnnotedID).findFirst().orElse(null);
    }

    private RS2Widget goldAmuletUCraftConfirm() {
        List<RS2Widget> allWidgets = getWidgets().getAll();
        return allWidgets.stream().filter(w -> w.getWidth() == 32 && w.getHeight() == 32 && w.getItemId() == goldAmuletUUnnotedID).findFirst().orElse(null);
    }

    private RS2Widget sapphireNecklaceCraftConfirm() {
        List<RS2Widget> allWidgets = getWidgets().getAll();
        return allWidgets.stream().filter(w -> w.getWidth() == 32 && w.getHeight() == 32 && w.getItemId() == sapphireNecklaceUnnotedID).findFirst().orElse(null);
    }

    private RS2Widget sapphireAmuletUCraftConfirm() {
        List<RS2Widget> allWidgets = getWidgets().getAll();
        return allWidgets.stream().filter(w -> w.getWidth() == 32 && w.getHeight() == 32 && w.getItemId() == sapphireAmuletUUnnotedID).findFirst().orElse(null);
    }

    private RS2Widget rubyNecklaceCraftConfirm() {
        List<RS2Widget> allWidgets = getWidgets().getAll();
        return allWidgets.stream().filter(w -> w.getWidth() == 32 && w.getHeight() == 32 && w.getItemId() == rubyNecklaceUnnotedID).findFirst().orElse(null);
    }

    private RS2Widget emeraldAmuletUCraftConfirm() {
        List<RS2Widget> allWidgets = getWidgets().getAll();
        return allWidgets.stream().filter(w -> w.getWidth() == 32 && w.getHeight() == 32 && w.getItemId() == emeraldAmuletUUnnotedID).findFirst().orElse(null);
    }

    private RS2Widget diamondRingCraftConfirm() {
        List<RS2Widget> allWidgets = getWidgets().getAll();
        return allWidgets.stream().filter(w -> w.getWidth() == 32 && w.getHeight() == 32 && w.getItemId() == diamondRingUnnotedID).findFirst().orElse(null);
    }

    private RS2Widget diamondNecklaceCraftConfirm() {
        List<RS2Widget> allWidgets = getWidgets().getAll();
        return allWidgets.stream().filter(w -> w.getWidth() == 32 && w.getHeight() == 32 && w.getItemId() == diamondNecklaceUnnotedID).findFirst().orElse(null);
    }

    private RS2Widget diamondAmuletUCraftConfirm() {
        List<RS2Widget> allWidgets = getWidgets().getAll();
        return allWidgets.stream().filter(w -> w.getWidth() == 32 && w.getHeight() == 32 && w.getItemId() == diamondAmuletUUnnotedID).findFirst().orElse(null);
    }

    private RS2Widget inventoryWidgetCheck() {
        List<RS2Widget> allWidgets = getWidgets().getAll();
        RS2Widget storedWidget = allWidgets.stream().filter(w -> w.getWidth() == 33 && w.getHeight() == 36 && w.getSpriteIndex1() == 900).findFirst().orElse(null);
        return storedWidget;
    }

    private GUI gui = new GUI();
    private CraftedItem craftedItem;

    boolean efficientLeveling = false;
    boolean profitLeveling = false;
    //NEEDS DUPLICATED
    boolean leatherGloveMatsCheck = false;

    boolean goldNecklaceMatsCheck = false;
    boolean busyWithGoldNecklaces;
    boolean craftingGoldNecklaces;

    boolean goldAmuletUMatsCheck = false;
    boolean busyWithGoldAmuletU;
    boolean craftingGoldAmuletU;

    boolean sapphireNecklaceMatsCheck = false;
    boolean busyWithSapphireNecklaces;
    boolean craftingSapphireNecklaces;

    boolean sapphireAmuletUMatsCheck = false;
    boolean busyWithSapphireAmuletU;
    boolean craftingSapphireAmuletU;

    boolean emeraldAmuletUMatsCheck = false;
    boolean busyWithEmeraldAmuletU;
    boolean craftingEmeraldAmuletU;

    boolean rubyNecklaceMatsCheck = false;
    boolean busyWithRubyNecklaces;
    boolean craftingRubyNecklaces;

    boolean diamondRingMatsCheck = false;
    boolean busyWithDiamondRings;
    boolean craftingDiamondRings;

    boolean diamondNecklaceMatsCheck = false;
    boolean busyWithDiamondNecklaces;
    boolean craftingDiamondNecklaces;

    boolean diamondAmuletUMatsCheck = false;
    boolean busyWithDiamondAmuletU;
    boolean craftingDiamondAmuletU;


    int totalValue;
    int startingCraftingEXP;
    int valueOfItem;
    int craftedItemValue;
    long startTime;
    int totalItemsCrafted;
    String status = "Starting...";

    RS2Object edgeBank;
    RS2Object geBank;

    public static File newImageFile = new File(System.getProperty("user.home") + File.separator + "OSBot" + File.separator + "Data" + File.separator + "newtestBackground.png");

    BufferedImage newImage;

    Area grandExchangeArea = new Area (3156,3480, 3172,3498);
    Area edgevilleArea = new Area (3089,3484,3114,3506);
    Area edgeBankArea = new Area (3092,3498,3099,3494);
    Area edgeFurnaceArea = new Area (3105,3496,3109,3501);

    Point mapLocationLow = new Point (622,60);
    Point mapLocationHigh = new Point (665,102);
    Point clientLocationLow = new Point (167,112);
    Point clientLocationHigh = new Point (416,287);

    @Override
    public void onStart() {
        //NEEDS DUPLICATED
        busyWithGoldNecklaces = false;
        craftingGoldNecklaces = false;

        busyWithGoldAmuletU = false;
        craftingGoldAmuletU = false;

        busyWithSapphireNecklaces = false;
        craftingSapphireNecklaces = false;

        busyWithSapphireAmuletU = false;
        craftingSapphireAmuletU = false;

        busyWithEmeraldAmuletU = false;
        craftingEmeraldAmuletU = false;

        busyWithRubyNecklaces = false;
        craftingRubyNecklaces = false;

        busyWithDiamondRings = false;
        craftingDiamondRings = false;

        busyWithDiamondNecklaces = false;
        craftingDiamondNecklaces = false;

        busyWithDiamondAmuletU = false;
        craftingDiamondAmuletU = false;

        totalItemsCrafted =0;
        totalValue = 0;
        valueOfItem = 0;
        craftedItemValue = 0;
        log("script started");
        startingCraftingEXP = getSkills().getExperience(Skill.CRAFTING);
        startTime = System.currentTimeMillis();
        try {
            newImage = ImageIO.read(newImageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            SwingUtilities.invokeAndWait(() -> {
                gui = new GUI();
                gui.open();
            });
        } catch (InterruptedException | InvocationTargetException e) {
            e.printStackTrace();
            stop();
            return;
        }

        //ends script if dialog was closed without selections
        if (!gui.isStarted()) {
            log("no options selected");
            return;
        }

        craftedItem = gui.getSelectedCraftedItem();
        if (craftedItem == CraftedItem.Efficient_leveling_with_profit){
            efficientLeveling = true;
        } else if (craftedItem == CraftedItem.Max_profit_leveling) {
            profitLeveling = true;
        }
        getSettings().setSetting(Settings.AllSettingsTab.INTERFACES,"Disable level-up interface", true);
        if (inventoryWidgetCheck() != null){
        }
    }

    public void buyFromGE(int itemId, String searchTerm , int price , int quantity ){
        grandExchange.buyItem(itemId, searchTerm,price,quantity );
    }

    public void moveMouseToMinimap(){
        if (getMouse().getPosition().x < mapLocationLow.x || getMouse().getPosition().x > mapLocationHigh.x){
            getMouse().move(random(mapLocationLow.x,mapLocationHigh.x),random(mapLocationLow.y, mapLocationHigh.y));
            new ConditionalSleep(3500){
                @Override
                public boolean condition() {
                    return getMouse().getPosition().x > mapLocationLow.x && getMouse().getPosition().x < mapLocationHigh.x;
                }
            }.sleep();
        }
    }
    //NEEDS DUPLICATED
//if i am crafting ruby necklace toggle something saying crafting ruby necklace that will bypass level restrictions and allow me to call ruby necklace method while level is too high until we are done crafting necklaces
    int goldNecklaceUnnotedID = 1654;
    int goldNecklaceNotedID = 1655;

    int goldAmuletUUnnotedID = 1692;
    int goldAmuletUNotedID = 1693;

    int sapphireUnnotedID = 1607;
    int sapphireNotedID = 1608;
    int sapphireNecklaceUnnotedID = 1656;
    int sapphireNecklaceNotedID = 1657;

    int sapphireAmuletUUnnotedID = 1675;
    int sapphireAmuletUNotedID = 1676;

    int emeraldUnnotedID = 1605;
    int emeraldNotedID = 1606;
    int emeraldAmuletUUnnotedID = 1677;
    int emeraldAmuletUNotedID = 1678;

    int rubyUnnotedID =1603;
    int rubyNotedID =1604;
    int rubyNecklaceUnnotedID =1660;
    int rubyNecklaceNotedID =1661;

    int diamondUnnotedID =1601;
    int diamondNotedID =1602;
    int diamondRingUnnotedID = 1643;
    int diamondRingNotedID = 1644;

    int diamondNecklaceUnnotedID = 1662;
    int diamondNecklaceNotedID = 1663;

    int diamondAmuletUUnnotedID = 1681;
    int diamondAmuletUNotedID = 1682;



    int necklaceMouldID = 1597;
    int amuletMouldID = 1595;
    int ringMouldID = 1592;
    int goldBarUnnotedID =2357;
    int goldBarNotedID =2358;

    public void rubyNecklace() throws InterruptedException {
        //NEEDS DUPLICATED
        craftingRubyNecklaces = true;
        log("ruby necklace selected...");
        log(status);

        boolean hasGold;
        boolean hasEnoughGold = false;
        boolean hasNecklaceMould = false;
        boolean hasRuby;
        boolean enoughRuby  = false;
        int intRequiredGold = 0;
        int intRequiredRuby = 0;
        log("have we check materials?"+rubyNecklaceMatsCheck);
        log("has enough ruby..."+enoughRuby);
        log("has enough gold..."+hasEnoughGold);

        if (Objects.equals(status, "Selling...")){
            busyWithRubyNecklaces = true;
            if (grandExchangeArea.contains(myPosition())){
                if (inventory.contains(rubyNecklaceUnnotedID) || inventory.contains(rubyNecklaceNotedID)){
                    NPC geClerk = getNpcs().closest("Grand Exchange Clerk");
                    if (geClerk != null){
                        if (geConfirmWidget() == null){
                            geClerk.interact("Exchange");
                            getMouse().move(random(clientLocationLow.x,clientLocationHigh.x),random(clientLocationLow.y, clientLocationHigh.y));
                            new ConditionalSleep(5000){
                                @Override
                                public boolean condition() {
                                    return geConfirmWidget()!= null;
                                }
                            }.sleep();
                        }
                        if (geConfirmWidget()!= null){
                            if (collectWidget()!= null && getInventory().contains(rubyNecklaceNotedID)){
                                collectWidget().interact();
                                status = "checking materials...";
                            }
                            if (getInventory().contains(rubyNecklaceUnnotedID) ||  inventory.contains(rubyNecklaceNotedID)){
                                grandExchange.sellItem(rubyNecklaceNotedID,1030,Math.toIntExact(getInventory().getAmount(rubyNecklaceNotedID)));
                                new ConditionalSleep(350000){
                                    @Override
                                    public boolean condition() {
                                        return collectWidget()!= null;
                                    }
                                }.sleep();
                                if (collectWidget()!= null && !getInventory().contains(rubyNecklaceNotedID)){
                                    collectWidget().interact();
                                    status = "checking materials...";
                                    busyWithRubyNecklaces = false;
                                } else {
                                   log("couldnt sell crafted items for gold closing script....");
                                   stop();
                                }
                            } else {
                                log("Couldnt identify or sell ruby necklaces...");
                            }

                        } else {
                            log("Issue with ge widget");
                        }
                    }
                }else {
                    bank.open();
                    if (bank.getWithdrawMode().equals((Bank.BankMode.WITHDRAW_NOTE))){
                        bank.withdrawAll(rubyNecklaceUnnotedID);
                    } else {
                        bank.enableMode(Bank.BankMode.WITHDRAW_NOTE);
                    }
                }

            } else {
                moveMouseToMinimap();
                getWalking().webWalk(Banks.GRAND_EXCHANGE);
            }
        }


        if (!rubyNecklaceMatsCheck && !Objects.equals(status, "Selling...")){
            if (grandExchangeArea.contains(myPosition())){
                if (getInventory().contains(rubyNecklaceUnnotedID) || getInventory().contains(rubyNecklaceNotedID)){
                    status = "Selling...";
                } else {
                    bank.open();
                    if (bank.isOpen()){
                        if (bank.contains(rubyNecklaceUnnotedID)){
                            if (bank.getWithdrawMode().equals((Bank.BankMode.WITHDRAW_NOTE))){
                                bank.withdrawAll(rubyNecklaceUnnotedID);
                            } else {
                                bank.enableMode(Bank.BankMode.WITHDRAW_NOTE);
                            }
                        } else if (!bank.contains(rubyNecklaceUnnotedID) && !getInventory().contains(rubyNecklaceUnnotedID) && !bank.contains(rubyNecklaceNotedID) && !getInventory().contains(rubyNecklaceNotedID)){
                            status = "Checking materials...";
                            busyWithRubyNecklaces = false;
                            if (bank.getWithdrawMode().equals((Bank.BankMode.WITHDRAW_ITEM))){
                                busyWithRubyNecklaces = false;
                                bank.depositAll();
                                sleep(random(1250,2250));
                                //if profit leveling
                                if (profitLeveling){
                                    if (bank.contains("Coins")){
                                        long amountOfCoins = bank.getAmount("Coins");
                                        log("amount of coins in bank..."+amountOfCoins);
                                        if (amountOfCoins >= 2000000){
                                            intRequiredGold = 1958;
                                            intRequiredRuby = 1958;
                                        } else if (amountOfCoins >= 1500000){
                                            intRequiredGold = 1500;
                                            intRequiredRuby = 1500;
                                        }else if (amountOfCoins >= 1000000){
                                            intRequiredGold = 1000;
                                            intRequiredRuby = 1000;
                                        } else if (amountOfCoins >=750000){
                                            intRequiredGold = 750;
                                            intRequiredRuby = 750;
                                        } else if (amountOfCoins >= 500000){
                                            intRequiredGold = 500;
                                            intRequiredRuby = 500;
                                        } else if (amountOfCoins >= 250000){
                                            intRequiredGold = 280;
                                            intRequiredRuby = 280;
                                        } else {
                                            log("Not enough coins");
                                            intRequiredGold = 0;
                                            intRequiredRuby =0;
                                            stop(false);
                                        }
                                    }
                                } else if (efficientLeveling && bank.contains("Coins")){
                                    long amountOfCoins = bank.getAmount("Coins");
                                    log("amount of coins in bank..."+amountOfCoins);
                                    //175 bars and amulets max
                                    if (amountOfCoins >= 175000){
                                        intRequiredGold = 175;
                                        intRequiredRuby = 175;
                                    } else {
                                        log("Not enough coins");
                                        intRequiredGold = 0;
                                        intRequiredRuby =0;
                                        stop(false);
                                    }
                                }

                                //if efficient leveling

                                if (bank.contains("Gold bar")){
                                    hasGold = true;
                                    log("has gold :"+hasGold);
                                } else {
                                    hasGold = false;
                                    log("has gold :"+hasGold);
                                }
                                if (hasGold){
                                    //if gold bars in bank and inventory is equal to what we need then we have enough gold
                                    if (bank.getItem("Gold bar").getAmount() >= intRequiredGold){
                                        hasEnoughGold = true;
                                        log("has enough gold :"+hasEnoughGold);
                                    } else {
                                        log("has enough gold :"+hasEnoughGold);
                                        long goldAmount = bank.getAmount("Gold bar");
                                        long inventoryGold = getInventory().getAmount(goldBarNotedID)+getInventory().getAmount(goldBarUnnotedID);
                                        long requiredGold = (intRequiredGold-goldAmount-inventoryGold);
                                        intRequiredGold = Math.toIntExact(requiredGold);
                                    }
                                }
                                if (bank.contains("Necklace mould") || getInventory().contains("Necklace mould")){
                                    hasNecklaceMould = true;
                                    log("has necklace mould :"+hasNecklaceMould);
                                } else {
                                    hasNecklaceMould = false;
                                    log("has necklace mould :"+hasNecklaceMould);
                                }
                                if (bank.contains("Ruby") || getInventory().contains("Ruby")){
                                    hasRuby = true;
                                    log("has ruby :"+hasRuby);
                                    if (bank.getItem("Ruby").getAmount() >=intRequiredRuby){
                                        enoughRuby = true;
                                        log("enough ruby :"+enoughRuby);
                                    } else {
                                        log("enough ruby :"+enoughRuby);
                                        long rubyAmount = bank.getAmount(rubyUnnotedID);
                                        long inventoryRuby = getInventory().getAmount(rubyUnnotedID) + getInventory().getAmount(rubyNotedID);
                                        long requiredRuby = (intRequiredRuby-(rubyAmount-inventoryRuby));
                                        intRequiredRuby = Math.toIntExact(requiredRuby);
                                    }
                                } else {
                                    hasRuby = false;
                                    log("has ruby :"+hasRuby);
                                }
                                if (enoughRuby && hasNecklaceMould && hasEnoughGold  || getInventory().contains(goldBarNotedID) && getInventory().contains(rubyNotedID) && !getInventory().contains(rubyNecklaceNotedID)){
                                    status ="Crafting...";
                                } else {
                                    if (!getInventory().contains(rubyNecklaceNotedID)){
                                        log("Couldnt identify necklaces so not selling any...");
                                        status = "Restocking...";

                                    }
                                    if (getInventory().contains(rubyNotedID) && getInventory().contains(goldBarNotedID)){
                                        enoughRuby = true;
                                        log("enough ruby :"+enoughRuby);
                                        hasEnoughGold = true;
                                        log("has enough gold :"+hasEnoughGold);
                                        status ="Crafting...";
                                    }

                                }
                                rubyNecklaceMatsCheck = true;
                                sleep(random(650,1250));
                            } else {
                                bank.enableMode(Bank.BankMode.WITHDRAW_ITEM);
                            }
                        }
                    }
                }

            } else if (edgevilleArea.contains(myPosition())){
                log("checking for materials in edge bank...");
                log("ruby necklace materials check.:"+rubyNecklaceMatsCheck);
                if (!rubyNecklaceMatsCheck){
                    if (edgeBankArea.contains(myPosition())){
                        edgeBank = getObjects().closest("Bank booth");
                        if (edgeBank!= null){
                            bank.open();
                            bank.depositAll();
                            if (bank.getWithdrawMode().equals((Bank.BankMode.WITHDRAW_ITEM))){
                                if (bank.contains(rubyUnnotedID) && bank.contains(goldBarUnnotedID) && bank.contains("Necklace mould")){
                                    rubyNecklaceMatsCheck = true;
                                    hasEnoughGold = true;
                                    enoughRuby = true;
                                    status = "Crafting...";
                                    busyWithRubyNecklaces = true;
                                } else {
                                    busyWithRubyNecklaces = false;
                                    log("testing123");
                                    status = "Restocking...";
                                    moveMouseToMinimap();
                                    getWalking().webWalk(Banks.GRAND_EXCHANGE);

                                }
                            } else {
                                bank.enableMode(Bank.BankMode.WITHDRAW_ITEM);
                            }

                        } else {
                            log("cant identify bank booth...");
                        }
                    } else {
                        log("not in edge bank!");
                        moveMouseToMinimap();
                        getWalking().walk(edgeBankArea);

                    }
                }
            }else {
                moveMouseToMinimap();
                getWalking().webWalk(Banks.GRAND_EXCHANGE);

            }
        }

        //if we have verified that we have all the necessary items to craft for this level bracket then we will start crafting
        if (Objects.equals(status,"Crafting...")){
            busyWithRubyNecklaces = true;
            log("we are ready to craft...");
            if (edgevilleArea.contains(myPosition())){
                log("edge bank111");
                if (getInventory().contains(goldBarNotedID)){
                    bank.open();
                    bank.depositAll(goldBarNotedID);
                }
                if (getInventory().contains(rubyNotedID)){
                    bank.open();
                    bank.depositAll(rubyNotedID);
                }
                if (getInventory().contains("Necklace mould") && getInventory().contains("Ruby") && getInventory().contains("gold bar")){
                    log("All items ready going to craft...");
                    if (getSettings().getRunEnergy() <60){
                        getSettings().setRunning(false);
                    }
                    moveMouseToMinimap();
                    getWalking().webWalk(edgeFurnaceArea);

                    RS2Object edgeFurnace = getObjects().closest("Furnace");
                    if (edgeFurnace != null){
                        edgeFurnace.interact();
                        getMouse().move(random(clientLocationLow.x,clientLocationHigh.x),random(clientLocationLow.y, clientLocationHigh.y));
                        new ConditionalSleep(3500){
                            @Override
                            public boolean condition(){
                                return rubyNecklaceCraftConfirm() != null;
                            }
                        }.sleep();
                    }
                    if (rubyNecklaceCraftConfirm() != null){
                        sleep(random(650,1500));
                        rubyNecklaceCraftConfirm().interact();
                        getMouse().moveOutsideScreen();
                        new ConditionalSleep(25000){
                            @Override
                            public boolean condition(){
                                return !getInventory().contains("Gold bar");
                            }
                        }.sleep();
                        sleep(random(850,3500));
                        getSettings().setRunning(true);
                    }
                } else {
                    log("Couldnt identify resources in inventory...");
                    if (edgeBankArea.contains(myPosition())){
                        log("edge bank222");
                        if (bank.isOpen()){
                            sleep(random(650,1500));
                            if (getInventory().contains(rubyNecklaceUnnotedID)){
                                bank.deposit(rubyNecklaceUnnotedID, 13);
                                totalItemsCrafted = 0;
                                bank.depositAll(goldBarUnnotedID);
                                bank.depositAll(rubyUnnotedID);
                            }
                            if (bank.contains("Necklace mould") && !getInventory().contains("Necklace mould")){
                                bank.withdraw("Necklace mould",1);
                            }
                            if (bank.contains("Ruby") && !getInventory().contains("Ruby")){
                                bank.withdraw("Ruby",13);
                            } else if (!bank.contains("Ruby")&& !getInventory().contains("Ruby")){
                                status = "Checking materials...";
                                enoughRuby = false;
                                rubyNecklaceMatsCheck = false;
                                status = "Selling...";
                            }
                            if (bank.contains("Gold bar") && !getInventory().contains("Gold bar")){
                                bank.withdraw("Gold bar",13);
                            } else if (!bank.contains("Gold bar")&& !getInventory().contains("Gold bar")){
                                status = "Checking materials...";
                                hasEnoughGold = false;
                                rubyNecklaceMatsCheck = false;
                                status = "Selling...";
                            }
                            bank.close();
                        } else {
                            bank.open();
                        }
                    } else {
                        moveMouseToMinimap();
                        getWalking().walk(edgeBankArea);

                    }
                }
            } else {
                getSettings().setRunning(true);
                moveMouseToMinimap();
                getWalking().webWalk(edgeBankArea);

            }
        }


        //buys items that we are missing from the ge for crafting
        if (Objects.equals(status,"Restocking...") && rubyNecklaceMatsCheck && getSkills().getStatic(Skill.CRAFTING) < 56){
            if (grandExchangeArea.contains(myPosition())){
                geBank = getObjects().closest(10060);
                NPC geClerk = getNpcs().closest("Grand Exchange Clerk");
                if (geClerk != null){
                    log("ge clerk found");
                    if (geConfirmWidget() == null){
                        geClerk.interact("Exchange");
                        getMouse().move(random(clientLocationLow.x,clientLocationHigh.x),random(clientLocationLow.y, clientLocationHigh.y));
                        new ConditionalSleep(5000){
                            @Override
                            public boolean condition() {
                                return geConfirmWidget()!= null;
                            }
                        }.sleep();
                    }
                    if (geConfirmWidget()!= null){
                        log("Required gold is :"+intRequiredGold);
                        log("Gold in inventory is..."+getInventory().getAmount("Gold bar"));
                        log("Required ruby is :"+intRequiredRuby);

                        if (craftedItemValue == 2){
                            craftedItemValue=(getGrandExchange().getOverallPrice(rubyNecklaceUnnotedID)-((getGrandExchange().getOverallPrice(goldBarUnnotedID)+getGrandExchange().getOverallPrice(rubyUnnotedID))));
                            log("ge setting profit ..."+craftedItemValue);
                            log("Price of crafted item...:"+getGrandExchange().getOverallPrice(rubyNecklaceUnnotedID));
                            log("Price of Gold:"+getGrandExchange().getOverallPrice(goldBarUnnotedID));
                            log("Price of ruby:"+getGrandExchange().getOverallPrice(rubyUnnotedID));
                        }

                        if (intRequiredGold == 0){
                            hasEnoughGold = true;

                        }
                        if (getInventory().getAmount("Necklace mould") >= 1){
                            hasNecklaceMould = true;
                        }
                        if (intRequiredRuby == 0){
                            hasRuby = true;
                        }

                        if (!hasEnoughGold && intRequiredGold!=0){
                            buyFromGE(goldBarUnnotedID,"Gold bar",130,intRequiredGold);
                            sleep(random(850,1850));
                            hasEnoughGold = true;
                        }
                        if (!hasNecklaceMould&& !getInventory().contains("Necklace mould")){
                            buyFromGE(necklaceMouldID,"Necklace mould",800,1);
                            sleep(random(850,1850));
                            hasNecklaceMould = true;
                        }
                        if (!enoughRuby&& intRequiredRuby!=0){
                            buyFromGE(rubyUnnotedID,"Ruby",875,intRequiredRuby);
                            sleep(random(850,1850));
                            enoughRuby = true;
                        }
                        new ConditionalSleep(3500){
                            @Override
                            public boolean condition() {
                                return collectWidget()!= null;
                            }
                        }.sleep();
                        if (collectWidget()!= null){
                            collectWidget().interact();
                            if (hasEnoughGold && enoughRuby){
                                status = "Crafting...";
                            }
                            log("settingstatustest123");
                            getMouse().move(random(clientLocationLow.x,clientLocationHigh.x),random(clientLocationLow.y, clientLocationHigh.y));
                        }
                    }
                }
            } else {
                moveMouseToMinimap();
                getWalking().webWalk(Banks.GRAND_EXCHANGE);

            }

        }
    }

    public void leatherGloves() throws InterruptedException {
        boolean hasLeather;
        boolean hasEnoughLeather = false;
        boolean hasNeedle = false;
        boolean hasThread;
        boolean enoughThread  = false;
        int intRequiredLeather = 38;
        int intRequiredThread = 15;
        if (grandExchangeArea.contains(myPosition())){
            geBank = getObjects().closest(10060);
            if (geBank!= null){
                log("ge Bank not null!");
                //if we have verified that we have all the necessary items to craft for this level bracket then we will start crafting
                if (Objects.equals(status,"Crafting gloves...")){
                    log("we are ready to craft gloves...");
                    if (geBank != null){
                        if (getInventory().contains("Needle") && getInventory().contains("Thread") && getInventory().contains("Leather")){
                            getInventory().getItem("Needle").interact();
                            getInventory().getItem("Leather").interact();
                            getMouse().move(random(clientLocationLow.x,clientLocationHigh.x),random(clientLocationLow.y, clientLocationHigh.y));
                            new ConditionalSleep(1750){
                                @Override
                                public boolean condition() {
                                    return leatherGloveCraftConfirm() != null;
                                }
                            }.sleep();
                            if (leatherGloveCraftConfirm() != null){
                                leatherGloveCraftConfirm().interact();
                                getMouse().move(random(clientLocationLow.x,clientLocationHigh.x),random(clientLocationLow.y, clientLocationHigh.y));
                                new ConditionalSleep(25000){
                                    @Override
                                    public boolean condition() {
                                        return !getInventory().contains("Leather");
                                    }
                                }.sleep();
                            }
                        } else {
                            if (bank.isOpen()){
                                if (getInventory().contains("Leather gloves")){
                                    bank.deposit("Leather gloves", 26);
                                }
                                if (bank.contains("Needle") && !getInventory().contains("Needle")){
                                    bank.withdraw("Needle",1);
                                }
                                if (bank.contains("Thread") && !getInventory().contains("Thread")){
                                    bank.withdraw("Thread",15);
                                }
                                if (bank.contains("Leather") && !getInventory().contains("Leather")){
                                    bank.withdraw("Leather",26);
                                }
                                bank.close();
                            } else {
                                bank.open();
                            }
                        }

                    }
                }
                //if we havent verified if we have the materials or not this will check our bank for everything that we are missing
                if (!leatherGloveMatsCheck){
                    status = "Checking materials";
                    bank.open();
                    bank.depositAll();
                    if (bank.isOpen()){
                        if (bank.getWithdrawMode().equals((Bank.BankMode.WITHDRAW_ITEM))){
                            if (bank.contains("Leather")){
                                hasLeather = true;
                                log("has leather :"+hasLeather);
                            } else {
                                hasLeather = false;
                                log("has leather :"+hasLeather);
                            }
                            if (hasLeather){
                                if (bank.getItem("Leather").getAmount() >=38){
                                    hasEnoughLeather = true;
                                    log("has enough leather :"+hasEnoughLeather);
                                } else {
                                    log("has enough leather :"+hasEnoughLeather);
                                    long leatherAmount = bank.getAmount("Leather");
                                    long inventoryLeather = getInventory().getAmount("Leather");
                                    long requiredLeather = 38 - leatherAmount - inventoryLeather;
                                    intRequiredLeather = Math.toIntExact(requiredLeather);
                                }
                            }

                            if (bank.contains("Needle")){
                                hasNeedle = true;
                                log("has needle :"+hasNeedle);
                            } else {
                                hasNeedle = false;
                                log("has needle :"+hasNeedle);
                            }

                            if (bank.contains("Thread")){
                                hasThread = true;
                                log("has thread :"+hasThread);
                                if (bank.getItem("Thread").getAmount() >=15){
                                    enoughThread = true;
                                    log("enough thread :"+enoughThread);
                                } else {
                                    log("enough thread :"+enoughThread);
                                    long threadAmount = bank.getAmount("Thread");
                                    long inventoryLeather = getInventory().getAmount("Thread");
                                    long requiredThread = 15 - threadAmount - inventoryLeather;
                                    intRequiredLeather = Math.toIntExact(requiredThread);
                                }
                            } else {
                                hasThread = false;
                                log("has thread :"+hasThread);
                            }
                            if (enoughThread && hasNeedle && hasEnoughLeather){
                                status ="Crafting gloves...";
                            } else {
                                status = "Restocking...";
                            }
                            leatherGloveMatsCheck = true;
                            sleep(random(650,1250));
                        } else {
                            bank.enableMode(Bank.BankMode.WITHDRAW_ITEM);
                        }



                    }
                }
                //buys items that we are missing from the ge for crafting
                if (Objects.equals(status,"Restocking...")){
                    NPC geClerk = getNpcs().closest("Grand Exchange Clerk");
                    if (geClerk != null){
                        if (geConfirmWidget() == null){
                            geClerk.interact("Exchange");
                            getMouse().move(random(clientLocationLow.x,clientLocationHigh.x),random(clientLocationLow.y, clientLocationHigh.y));
                            new ConditionalSleep(5000){
                                @Override
                                public boolean condition() {
                                    return geConfirmWidget()!= null;
                                }
                            }.sleep();
                        }
                        if (geConfirmWidget()!= null){
                            if (!hasEnoughLeather){
                                buyFromGE(1741,"Leather",200,intRequiredLeather);
                            if (!hasNeedle){
                                buyFromGE(1733,"Needle",200,1);
                            }
                            if (!enoughThread){
                                buyFromGE(1734,"Thread",200,intRequiredThread);
                            }
                                new ConditionalSleep(3500){
                                    @Override
                                    public boolean condition() {
                                        return collectWidget()!= null;
                                    }
                                }.sleep();
                                if (collectWidget()!= null){
                                    collectWidget().interact();
                                    getMouse().move(random(clientLocationLow.x,clientLocationHigh.x),random(clientLocationLow.y, clientLocationHigh.y));
                                }
                            }

                        }
                    }

                }
            }
        } else {
            moveMouseToMinimap();
            getWalking().webWalk(Banks.GRAND_EXCHANGE);

        }
    }

    public void goldNecklace() throws InterruptedException {
            //NEEDS DUPLICATED
            craftingGoldNecklaces = true;
            log("gold necklace selected...");
            log(status);

            boolean hasGold;
            boolean hasEnoughGold = false;
            boolean hasNecklaceMould = false;
            int intRequiredGold = 0;
            log("have we check materials?"+goldNecklaceMatsCheck);
            log("has enough gold..."+hasEnoughGold);

            if (Objects.equals(status, "Selling...")){
                busyWithGoldNecklaces = true;
                if (grandExchangeArea.contains(myPosition())){
                    if (inventory.contains(goldNecklaceUnnotedID) || inventory.contains(goldNecklaceNotedID)){
                        NPC geClerk = getNpcs().closest("Grand Exchange Clerk");
                        if (geClerk != null){
                            if (geConfirmWidget() == null){
                                geClerk.interact("Exchange");
                                getMouse().move(random(clientLocationLow.x,clientLocationHigh.x),random(clientLocationLow.y, clientLocationHigh.y));
                                new ConditionalSleep(5000){
                                    @Override
                                    public boolean condition() {
                                        return geConfirmWidget()!= null;
                                    }
                                }.sleep();
                            }
                            if (geConfirmWidget()!= null){
                                if (collectWidget()!= null && getInventory().contains(goldNecklaceNotedID)){
                                    collectWidget().interact();
                                    status = "checking materials...";
                                }
                                if (getInventory().contains(goldNecklaceUnnotedID) ||  inventory.contains(goldNecklaceNotedID)){
                                    grandExchange.sellItem(goldNecklaceNotedID,130,Math.toIntExact(getInventory().getAmount(goldNecklaceNotedID)));
                                    new ConditionalSleep(350000){
                                        @Override
                                        public boolean condition() {
                                            return collectWidget()!= null;
                                        }
                                    }.sleep();
                                    if (collectWidget()!= null && !getInventory().contains(goldNecklaceNotedID)){
                                        collectWidget().interact();
                                        status = "checking materials...";
                                        busyWithGoldNecklaces = false;
                                    } else {
                                        log("couldnt sell crafted items for gold closing script....");
                                        stop();
                                    }
                                } else {
                                    log("Couldnt identify or sell gold necklaces...");
                                /*
                                if (collectWidget()!= null && getInventory().contains(goldNecklaceNotedID)){
                                    collectWidget().interact();
                                    status = "checking materials...";
                                    busyWithGoldNecklaces = false;
                                } else {

                                }

                                 */
                                }


                            } else {
                                log("Issue with ge widget");
                            }
                        }
                    }else {
                        bank.open();
                        if (bank.getWithdrawMode().equals((Bank.BankMode.WITHDRAW_NOTE))){
                            bank.withdrawAll(goldNecklaceUnnotedID);
                        } else {
                            bank.enableMode(Bank.BankMode.WITHDRAW_NOTE);
                        }
                    }

                } else {
                    moveMouseToMinimap();
                    getWalking().webWalk(Banks.GRAND_EXCHANGE);
                }
            }


            if (!goldNecklaceMatsCheck && !Objects.equals(status, "Selling...")){
                if (grandExchangeArea.contains(myPosition())){
                    if (getInventory().contains(goldNecklaceUnnotedID) || getInventory().contains(goldNecklaceNotedID)){
                        status = "Selling...";
                    } else {
                        bank.open();
                        if (bank.isOpen()){
                            if (bank.contains(goldNecklaceUnnotedID)){
                                if (bank.getWithdrawMode().equals((Bank.BankMode.WITHDRAW_NOTE))){
                                    bank.withdrawAll(goldNecklaceUnnotedID);
                                } else {
                                    bank.enableMode(Bank.BankMode.WITHDRAW_NOTE);
                                }
                            } else if (!bank.contains(goldNecklaceUnnotedID) && !getInventory().contains(goldNecklaceUnnotedID) && !bank.contains(goldNecklaceNotedID) && !getInventory().contains(goldNecklaceNotedID)){
                                status = "Checking materials...";
                                busyWithGoldNecklaces = false;
                                if (bank.getWithdrawMode().equals((Bank.BankMode.WITHDRAW_ITEM))){
                                    busyWithGoldNecklaces = false;
                                    bank.depositAll();
                                    sleep(random(1250,2250));

                                    if (profitLeveling){
                                        if (bank.contains("Coins")){
                                            long amountOfCoins = bank.getAmount("Coins");
                                            log("amount of coins in bank..."+amountOfCoins);
                                            if (amountOfCoins >= 40000){
                                                intRequiredGold = 256;
                                            } else {
                                                log("Not enough coins");
                                                intRequiredGold = 0;
                                                stop(false);
                                            }
                                        }
                                    } else if (efficientLeveling && bank.contains("Coins")){
                                        long amountOfCoins = bank.getAmount("Coins");
                                        log("amount of coins in bank..."+amountOfCoins);
                                        //175 bars and amulets max
                                        if (amountOfCoins >= 15000){
                                            intRequiredGold = 15;
                                        } else {
                                            log("Not enough coins");
                                            intRequiredGold = 0;
                                            stop(false);
                                        }
                                    }

                                    if (bank.contains("Gold bar")){
                                        hasGold = true;
                                        log("has gold :"+hasGold);
                                    } else {
                                        hasGold = false;
                                        log("has gold :"+hasGold);
                                    }
                                    if (hasGold){
                                        //if gold bars in bank and inventory is equal to what we need then we have enough gold
                                        if (bank.getItem("Gold bar").getAmount() >= intRequiredGold){
                                            hasEnoughGold = true;
                                            log("has enough gold :"+hasEnoughGold);
                                        } else {
                                            log("has enough gold :"+hasEnoughGold);
                                            long goldAmount = bank.getAmount("Gold bar");
                                            long inventoryGold = getInventory().getAmount(goldBarNotedID)+getInventory().getAmount(goldBarUnnotedID);
                                            long requiredGold = (intRequiredGold-goldAmount-inventoryGold);
                                            intRequiredGold = Math.toIntExact(requiredGold);
                                        }
                                    }
                                    if (bank.contains("Necklace mould") || getInventory().contains("Necklace mould")){
                                        hasNecklaceMould = true;
                                        log("has necklace mould :"+hasNecklaceMould);
                                    } else {
                                        hasNecklaceMould = false;
                                        log("has necklace mould :"+hasNecklaceMould);
                                    }
                                    if (hasNecklaceMould && hasEnoughGold  || getInventory().contains(goldBarNotedID) && !getInventory().contains(goldNecklaceNotedID)){
                                        status ="Crafting...";
                                    } else {
                                        if (!getInventory().contains(goldNecklaceNotedID)){
                                            log("Couldnt identify necklaces so not selling any...");
                                            status = "Restocking...";

                                        }
                                        if (getInventory().contains(goldBarNotedID)){
                                            hasEnoughGold = true;
                                            log("has enough gold :"+hasEnoughGold);
                                            status ="Crafting...";
                                        }

                                    }
                                    goldNecklaceMatsCheck = true;
                                    sleep(random(650,1250));
                                } else {
                                    bank.enableMode(Bank.BankMode.WITHDRAW_ITEM);
                                }
                            }
                        }
                    }

                } else if (edgevilleArea.contains(myPosition())){
                    log("checking for materials in edge bank...");
                    log("gold necklace materials check.:"+goldNecklaceMatsCheck);
                    if (!goldNecklaceMatsCheck){
                        if (edgeBankArea.contains(myPosition())){
                            edgeBank = getObjects().closest("Bank booth");
                            if (edgeBank!= null){
                                bank.open();
                                bank.depositAll();
                                if (bank.getWithdrawMode().equals((Bank.BankMode.WITHDRAW_ITEM))){
                                    if (bank.contains(goldBarUnnotedID) && bank.contains("Necklace mould")){
                                        goldNecklaceMatsCheck = true;
                                        hasEnoughGold = true;
                                        status = "Crafting...";
                                        busyWithGoldNecklaces = true;
                                    } else {
                                        busyWithGoldNecklaces = false;
                                        log("testing123");
                                        status = "Restocking...";
                                        moveMouseToMinimap();
                                        getWalking().webWalk(Banks.GRAND_EXCHANGE);

                                    }
                                } else {
                                    bank.enableMode(Bank.BankMode.WITHDRAW_ITEM);
                                }

                            } else {
                                log("cant identify bank booth...");
                            }
                        } else {
                            log("not in edge bank!");
                            moveMouseToMinimap();
                            getWalking().walk(edgeBankArea);

                        }
                    }
                }else {
                    moveMouseToMinimap();
                    getWalking().webWalk(Banks.GRAND_EXCHANGE);

                }
            }

            //if we have verified that we have all the necessary items to craft for this level bracket then we will start crafting
            if (Objects.equals(status,"Crafting...")){
                busyWithGoldNecklaces = true;
                log("we are ready to craft...");
                if (edgevilleArea.contains(myPosition())){
                    log("edge bank111");
                    if (getInventory().contains(goldBarNotedID)){
                        bank.open();
                        bank.depositAll(goldBarNotedID);
                    }
                    if (getInventory().contains("Necklace mould") && getInventory().contains("gold bar")){
                        log("All items ready going to craft...");
                        if (getSettings().getRunEnergy() <60){
                            getSettings().setRunning(false);
                        }
                        moveMouseToMinimap();
                        getWalking().webWalk(edgeFurnaceArea);

                        RS2Object edgeFurnace = getObjects().closest("Furnace");
                        if (edgeFurnace != null){
                            edgeFurnace.interact();
                            getMouse().move(random(clientLocationLow.x,clientLocationHigh.x),random(clientLocationLow.y, clientLocationHigh.y));
                            new ConditionalSleep(3500){
                                @Override
                                public boolean condition(){
                                    return goldNecklaceCraftConfirm() != null;
                                }
                            }.sleep();
                        }
                        if (goldNecklaceCraftConfirm() != null){
                            sleep(random(650,1500));
                            goldNecklaceCraftConfirm().interact();
                            getMouse().moveOutsideScreen();
                            new ConditionalSleep(50000){
                                @Override
                                public boolean condition(){
                                    return !getInventory().contains("Gold bar");
                                }
                            }.sleep();
                            sleep(random(850,3500));
                            getSettings().setRunning(true);
                        }
                    } else {
                        log("Couldnt identify resources in inventory...");
                        if (edgeBankArea.contains(myPosition())){
                            log("edge bank222");
                            if (bank.isOpen()){
                                sleep(random(650,1500));
                                if (getInventory().contains(goldNecklaceUnnotedID)){
                                    bank.depositAll(goldNecklaceUnnotedID);
                                    totalItemsCrafted = 0;
                                    bank.depositAll(goldBarUnnotedID);
                                }
                                if (bank.contains("Necklace mould") && !getInventory().contains("Necklace mould")){
                                    bank.withdraw("Necklace mould",1);
                                }
                                if (bank.contains("Gold bar") && !getInventory().contains("Gold bar")){
                                    bank.withdraw("Gold bar",27);
                                } else if (!bank.contains("Gold bar")&& !getInventory().contains("Gold bar")){
                                    status = "Checking materials...";
                                    hasEnoughGold = false;
                                    goldNecklaceMatsCheck = false;
                                    status = "Selling...";
                                }
                                bank.close();
                            } else {
                                bank.open();
                            }
                        } else {
                            moveMouseToMinimap();
                            getWalking().walk(edgeBankArea);

                        }
                    }
                } else {
                    getSettings().setRunning(true);
                    moveMouseToMinimap();
                    getWalking().webWalk(edgeBankArea);

                }
            }


            //buys items that we are missing from the ge for crafting
            if (Objects.equals(status,"Restocking...") && goldNecklaceMatsCheck && getSkills().getStatic(Skill.CRAFTING) < 8 && efficientLeveling ||Objects.equals(status,"Restocking...") && goldNecklaceMatsCheck && getSkills().getStatic(Skill.CRAFTING) < 22 && profitLeveling){
                if (grandExchangeArea.contains(myPosition())){
                    geBank = getObjects().closest(10060);
                    NPC geClerk = getNpcs().closest("Grand Exchange Clerk");
                    if (geClerk != null){
                        log("ge clerk found");
                        if (geConfirmWidget() == null){
                            geClerk.interact("Exchange");
                            getMouse().move(random(clientLocationLow.x,clientLocationHigh.x),random(clientLocationLow.y, clientLocationHigh.y));
                            new ConditionalSleep(5000){
                                @Override
                                public boolean condition() {
                                    return geConfirmWidget()!= null;
                                }
                            }.sleep();
                        }
                        if (geConfirmWidget()!= null){
                            log("Required gold is :"+intRequiredGold);
                            log("Gold in inventory is..."+getInventory().getAmount("Gold bar"));

                            if (craftedItemValue == 2){
                                craftedItemValue=(getGrandExchange().getOverallPrice(goldNecklaceUnnotedID)-((getGrandExchange().getOverallPrice(goldBarUnnotedID))));
                                log("ge setting profit ..."+craftedItemValue);
                                log("Price of crafted item...:"+getGrandExchange().getOverallPrice(goldNecklaceUnnotedID));
                                log("Price of Gold:"+getGrandExchange().getOverallPrice(goldBarUnnotedID));

                            }

                            if (intRequiredGold == 0){
                                hasEnoughGold = true;

                            }
                            if (getInventory().getAmount("Necklace mould") >= 1){
                                hasNecklaceMould = true;
                            }

                            if (!hasEnoughGold && intRequiredGold!=0){
                                buyFromGE(goldBarUnnotedID,"Gold bar",130,intRequiredGold);
                                sleep(random(850,1850));
                                hasEnoughGold = true;
                            }
                            if (!hasNecklaceMould&& !getInventory().contains("Necklace mould")){
                                buyFromGE(necklaceMouldID,"Necklace mould",800,1);
                                sleep(random(850,1850));
                                hasNecklaceMould = true;
                            }

                            new ConditionalSleep(3500){
                                @Override
                                public boolean condition() {
                                    return collectWidget()!= null;
                                }
                            }.sleep();
                            if (collectWidget()!= null){
                                collectWidget().interact();
                                if (hasEnoughGold){
                                    status = "Crafting...";
                                }
                                log("settingstatustest123");
                                getMouse().move(random(clientLocationLow.x,clientLocationHigh.x),random(clientLocationLow.y, clientLocationHigh.y));
                            }
                        }
                    }
                } else {
                    moveMouseToMinimap();
                    getWalking().webWalk(Banks.GRAND_EXCHANGE);

                }

            }

    }

    public void goldAmuletU() throws InterruptedException {
            //NEEDS DUPLICATED
            craftingGoldAmuletU = true;
            log("gold necklace selected...");
            log(status);

            boolean hasGold;
            boolean hasEnoughGold = false;
            boolean hasAmuletMould = false;
            int intRequiredGold = 0;
            log("have we check materials?"+goldAmuletUMatsCheck);
            log("has enough gold..."+hasEnoughGold);

            if (Objects.equals(status, "Selling...")){
                busyWithGoldAmuletU = true;
                if (grandExchangeArea.contains(myPosition())){
                    if (inventory.contains(goldAmuletUUnnotedID) || inventory.contains(goldAmuletUNotedID)){
                        NPC geClerk = getNpcs().closest("Grand Exchange Clerk");
                        if (geClerk != null){
                            if (geConfirmWidget() == null){
                                geClerk.interact("Exchange");
                                getMouse().move(random(clientLocationLow.x,clientLocationHigh.x),random(clientLocationLow.y, clientLocationHigh.y));
                                new ConditionalSleep(5000){
                                    @Override
                                    public boolean condition() {
                                        return geConfirmWidget()!= null;
                                    }
                                }.sleep();
                            }
                            if (geConfirmWidget()!= null){
                                if (collectWidget()!= null && getInventory().contains(goldAmuletUNotedID)){
                                    collectWidget().interact();
                                    status = "checking materials...";
                                }
                                if (getInventory().contains(goldAmuletUUnnotedID) ||  inventory.contains(goldAmuletUNotedID)){
                                    grandExchange.sellItem(goldAmuletUNotedID,140,Math.toIntExact(getInventory().getAmount(goldAmuletUNotedID)));
                                    new ConditionalSleep(350000){
                                        @Override
                                        public boolean condition() {
                                            return collectWidget()!= null;
                                        }
                                    }.sleep();
                                    if (collectWidget()!= null && !getInventory().contains(goldAmuletUNotedID)){
                                        collectWidget().interact();
                                        status = "checking materials...";
                                        busyWithGoldAmuletU = false;
                                    } else {
                                        log("couldnt sell crafted items for gold closing script....");
                                        stop();
                                    }
                                } else {
                                    log("Couldnt identify or sell gold necklaces...");

                                }


                            } else {
                                log("Issue with ge widget");
                            }
                        }
                    }else {
                        bank.open();
                        if (bank.getWithdrawMode().equals((Bank.BankMode.WITHDRAW_NOTE))){
                            bank.withdrawAll(goldAmuletUUnnotedID);
                        } else {
                            bank.enableMode(Bank.BankMode.WITHDRAW_NOTE);
                        }
                    }

                } else {
                    moveMouseToMinimap();
                    getWalking().webWalk(Banks.GRAND_EXCHANGE);
                }
            }


            if (!goldAmuletUMatsCheck && !Objects.equals(status, "Selling...")){
                if (grandExchangeArea.contains(myPosition())){
                    if (getInventory().contains(goldAmuletUUnnotedID) || getInventory().contains(goldAmuletUNotedID)){
                        status = "Selling...";
                    } else {
                        bank.open();
                        if (bank.isOpen()){
                            if (bank.contains(goldAmuletUUnnotedID)){
                                if (bank.getWithdrawMode().equals((Bank.BankMode.WITHDRAW_NOTE))){
                                    bank.withdrawAll(goldAmuletUUnnotedID);
                                } else {
                                    bank.enableMode(Bank.BankMode.WITHDRAW_NOTE);
                                }
                            } else if (!bank.contains(goldAmuletUUnnotedID) && !getInventory().contains(goldAmuletUUnnotedID) && !bank.contains(goldAmuletUNotedID) && !getInventory().contains(goldAmuletUNotedID)){
                                status = "Checking materials...";
                                busyWithGoldAmuletU = false;
                                if (bank.getWithdrawMode().equals((Bank.BankMode.WITHDRAW_ITEM))){
                                    busyWithGoldAmuletU = false;
                                    bank.depositAll();
                                    sleep(random(1250,2250));
                                    if (profitLeveling){
                                        log("profit leveling wont craft gold amulets");
                                    } else if (efficientLeveling && bank.contains("Coins")){
                                        long amountOfCoins = bank.getAmount("Coins");
                                        log("amount of coins in bank..."+amountOfCoins);
                                        if (amountOfCoins >= 30000){
                                            intRequiredGold = 161;
                                        } else {
                                            log("Not enough coins");
                                            intRequiredGold = 0;
                                            stop(false);

                                        }
                                    }

                                    if (bank.contains("Gold bar")){
                                        hasGold = true;
                                        log("has gold :"+hasGold);
                                    } else {
                                        hasGold = false;
                                        log("has gold :"+hasGold);
                                    }
                                    if (hasGold){
                                        //if gold bars in bank and inventory is equal to what we need then we have enough gold
                                        if (bank.getItem("Gold bar").getAmount() >= intRequiredGold){
                                            hasEnoughGold = true;
                                            log("has enough gold :"+hasEnoughGold);
                                        } else {
                                            log("has enough gold :"+hasEnoughGold);
                                            long goldAmount = bank.getAmount("Gold bar");
                                            long inventoryGold = getInventory().getAmount(goldBarNotedID)+getInventory().getAmount(goldBarUnnotedID);
                                            long requiredGold = (intRequiredGold-goldAmount-inventoryGold);
                                            intRequiredGold = Math.toIntExact(requiredGold);
                                        }
                                    }
                                    if (bank.contains("Amulet mould") || getInventory().contains("Amulet mould")){
                                        hasAmuletMould = true;
                                        log("has amluet mould :"+hasAmuletMould);
                                    } else {
                                        hasAmuletMould = false;
                                        log("has amulet mould :"+hasAmuletMould);
                                    }
                                    if (hasAmuletMould && hasEnoughGold  || getInventory().contains(goldBarNotedID) && !getInventory().contains(goldAmuletUNotedID)){
                                        status ="Crafting...";
                                    } else {
                                        if (!getInventory().contains(goldAmuletUNotedID)){
                                            log("Couldnt identify necklaces so not selling any...");
                                            status = "Restocking...";

                                        }
                                        if (getInventory().contains(goldBarNotedID)){
                                            hasEnoughGold = true;
                                            log("has enough gold :"+hasEnoughGold);
                                            status ="Crafting...";
                                        }

                                    }
                                    goldAmuletUMatsCheck = true;
                                    sleep(random(650,1250));
                                } else {
                                    bank.enableMode(Bank.BankMode.WITHDRAW_ITEM);
                                }
                            }
                        }
                    }

                } else if (edgevilleArea.contains(myPosition())){
                    log("checking for materials in edge bank...");
                    log("gold necklace materials check.:"+goldAmuletUMatsCheck);
                    if (!goldAmuletUMatsCheck){
                        if (edgeBankArea.contains(myPosition())){
                            edgeBank = getObjects().closest("Bank booth");
                            if (edgeBank!= null){
                                bank.open();
                                bank.depositAll();
                                if (bank.getWithdrawMode().equals((Bank.BankMode.WITHDRAW_ITEM))){
                                    if (bank.contains(goldBarUnnotedID) && bank.contains("Amulet mould")){
                                        goldAmuletUMatsCheck = true;
                                        hasEnoughGold = true;
                                        status = "Crafting...";
                                        busyWithGoldAmuletU = true;
                                    } else {
                                        busyWithGoldAmuletU = false;
                                        log("testing123");
                                        status = "Restocking...";
                                        moveMouseToMinimap();
                                        getWalking().webWalk(Banks.GRAND_EXCHANGE);

                                    }
                                } else {
                                    bank.enableMode(Bank.BankMode.WITHDRAW_ITEM);
                                }

                            } else {
                                log("cant identify bank booth...");
                            }
                        } else {
                            log("not in edge bank!");
                            moveMouseToMinimap();
                            getWalking().walk(edgeBankArea);

                        }
                    }
                }else {
                    moveMouseToMinimap();
                    getWalking().webWalk(Banks.GRAND_EXCHANGE);

                }
            }

            //if we have verified that we have all the necessary items to craft for this level bracket then we will start crafting
            if (Objects.equals(status,"Crafting...")){
                busyWithGoldAmuletU = true;
                log("we are ready to craft...");
                if (edgevilleArea.contains(myPosition())){
                    log("edge bank111");
                    if (getInventory().contains(goldBarNotedID)){
                        bank.open();
                        bank.depositAll(goldBarNotedID);
                    }
                    if (getInventory().contains("Amulet mould") && getInventory().contains("gold bar")){
                        log("All items ready going to craft...");
                        if (getSettings().getRunEnergy() <60){
                            getSettings().setRunning(false);
                        }
                        moveMouseToMinimap();
                        getWalking().webWalk(edgeFurnaceArea);

                        RS2Object edgeFurnace = getObjects().closest("Furnace");
                        if (edgeFurnace != null){
                            edgeFurnace.interact();
                            getMouse().move(random(clientLocationLow.x,clientLocationHigh.x),random(clientLocationLow.y, clientLocationHigh.y));
                            new ConditionalSleep(3500){
                                @Override
                                public boolean condition(){
                                    return goldAmuletUCraftConfirm() != null;
                                }
                            }.sleep();
                        }
                        if (goldAmuletUCraftConfirm() != null){
                            sleep(random(650,1500));
                            goldAmuletUCraftConfirm().interact();
                            getMouse().moveOutsideScreen();
                            new ConditionalSleep(50000){
                                @Override
                                public boolean condition(){
                                    return !getInventory().contains("Gold bar");
                                }
                            }.sleep();
                            sleep(random(850,3500));
                            getSettings().setRunning(true);
                        }
                    } else {
                        log("Couldnt identify resources in inventory...");
                        if (edgeBankArea.contains(myPosition())){
                            log("edge bank222");
                            if (bank.isOpen()){
                                sleep(random(650,1500));
                                if (getInventory().contains(goldAmuletUUnnotedID)){
                                    bank.deposit(goldAmuletUUnnotedID, 13);
                                    totalItemsCrafted = 0;
                                    bank.depositAll(goldBarUnnotedID);
                                }
                                if (bank.contains("Amulet mould") && !getInventory().contains("Amulet mould")){
                                    bank.withdraw("Amulet mould",1);
                                }
                                if (bank.contains("Gold bar") && !getInventory().contains("Gold bar")){
                                    bank.withdraw("Gold bar",27);
                                } else if (!bank.contains("Gold bar")&& !getInventory().contains("Gold bar")){
                                    status = "Checking materials...";
                                    hasEnoughGold = false;
                                    goldAmuletUMatsCheck = false;
                                    status = "Selling...";
                                }
                                bank.close();
                            } else {
                                bank.open();
                            }
                        } else {
                            moveMouseToMinimap();
                            getWalking().walk(edgeBankArea);

                        }
                    }
                } else {
                    getSettings().setRunning(true);
                    moveMouseToMinimap();
                    getWalking().webWalk(edgeBankArea);

                }
            }


            //buys items that we are missing from the ge for crafting
            if (Objects.equals(status,"Restocking...") && goldAmuletUMatsCheck && getSkills().getStatic(Skill.CRAFTING) < 22){
                if (grandExchangeArea.contains(myPosition())){
                    geBank = getObjects().closest(10060);
                    NPC geClerk = getNpcs().closest("Grand Exchange Clerk");
                    if (geClerk != null){
                        log("ge clerk found");
                        if (geConfirmWidget() == null){
                            geClerk.interact("Exchange");
                            getMouse().move(random(clientLocationLow.x,clientLocationHigh.x),random(clientLocationLow.y, clientLocationHigh.y));
                            new ConditionalSleep(5000){
                                @Override
                                public boolean condition() {
                                    return geConfirmWidget()!= null;
                                }
                            }.sleep();
                        }
                        if (geConfirmWidget()!= null){
                            log("Required gold is :"+intRequiredGold);
                            log("Gold in inventory is..."+getInventory().getAmount("Gold bar"));

                            if (craftedItemValue == 2){
                                craftedItemValue=(getGrandExchange().getOverallPrice(goldAmuletUUnnotedID)-((getGrandExchange().getOverallPrice(goldBarUnnotedID))));
                                log("ge setting profit ..."+craftedItemValue);
                                log("Price of crafted item...:"+getGrandExchange().getOverallPrice(goldAmuletUUnnotedID));
                                log("Price of Gold:"+getGrandExchange().getOverallPrice(goldBarUnnotedID));

                            }

                            if (intRequiredGold == 0){
                                hasEnoughGold = true;

                            }
                            if (getInventory().getAmount("Amulet mould") >= 1){
                                hasAmuletMould = true;
                            }

                            if (!hasEnoughGold && intRequiredGold!=0){
                                buyFromGE(goldBarUnnotedID,"Gold bar",150,intRequiredGold);
                                sleep(random(850,1850));
                                hasEnoughGold = true;
                            }
                            if (!hasAmuletMould&& !getInventory().contains("Amulet mould")){
                                buyFromGE(amuletMouldID,"Amulet mould",800,1);
                                sleep(random(850,1850));
                                hasAmuletMould = true;
                            }

                            new ConditionalSleep(3500){
                                @Override
                                public boolean condition() {
                                    return collectWidget()!= null;
                                }
                            }.sleep();
                            if (collectWidget()!= null){
                                collectWidget().interact();
                                if (hasEnoughGold){
                                    status = "Crafting...";
                                }
                                log("settingstatustest123");
                                getMouse().move(random(clientLocationLow.x,clientLocationHigh.x),random(clientLocationLow.y, clientLocationHigh.y));
                            }
                        }
                    }
                } else {
                    moveMouseToMinimap();
                    getWalking().webWalk(Banks.GRAND_EXCHANGE);

                }

            }

    }

    public void sapphireNecklace() throws InterruptedException {

        //NEEDS DUPLICATED
        craftingSapphireNecklaces = true;
        log("sapphire necklace selected...");
        log(status);

        boolean hasGold;
        boolean hasEnoughGold = false;
        boolean hasNecklaceMould = false;
        boolean hasSapphire;
        boolean enoughSapphire  = false;
        int intRequiredGold = 0;
        int intRequiredSapphire = 0;
        log("have we check materials?"+sapphireNecklaceMatsCheck);
        log("has enough sapphire..."+enoughSapphire);
        log("has enough gold..."+hasEnoughGold);

        if (Objects.equals(status, "Selling...")){
            busyWithSapphireNecklaces = true;
            if (grandExchangeArea.contains(myPosition())){
                if (inventory.contains(sapphireNecklaceUnnotedID) || inventory.contains(sapphireNecklaceNotedID)){
                    NPC geClerk = getNpcs().closest("Grand Exchange Clerk");
                    if (geClerk != null){
                        if (geConfirmWidget() == null){
                            geClerk.interact("Exchange");
                            getMouse().move(random(clientLocationLow.x,clientLocationHigh.x),random(clientLocationLow.y, clientLocationHigh.y));
                            new ConditionalSleep(5000){
                                @Override
                                public boolean condition() {
                                    return geConfirmWidget()!= null;
                                }
                            }.sleep();
                        }
                        if (geConfirmWidget()!= null){
                            if (collectWidget()!= null && getInventory().contains(sapphireNecklaceNotedID)){
                                collectWidget().interact();
                                status = "checking materials...";
                            }
                            if (getInventory().contains(sapphireNecklaceUnnotedID) ||  inventory.contains(sapphireNecklaceNotedID)){
                                grandExchange.sellItem(sapphireNecklaceNotedID,440,Math.toIntExact(getInventory().getAmount(sapphireNecklaceNotedID)));
                                new ConditionalSleep(350000){
                                    @Override
                                    public boolean condition() {
                                        return collectWidget()!= null;
                                    }
                                }.sleep();
                                if (collectWidget()!= null && !getInventory().contains(sapphireNecklaceNotedID)){
                                    collectWidget().interact();
                                    status = "checking materials...";
                                    busyWithSapphireNecklaces = false;
                                } else {
                                    log("couldnt sell crafted items for gold closing script....");
                                    stop();
                                }
                            } else {
                                log("Couldnt identify or sell sapphire necklaces...");
                            }

                        } else {
                            log("Issue with ge widget");
                        }
                    }
                }else {
                    bank.open();
                    if (bank.getWithdrawMode().equals((Bank.BankMode.WITHDRAW_NOTE))){
                        bank.withdrawAll(sapphireNecklaceUnnotedID);
                    } else {
                        bank.enableMode(Bank.BankMode.WITHDRAW_NOTE);
                    }
                }

            } else {
                moveMouseToMinimap();
                getWalking().webWalk(Banks.GRAND_EXCHANGE);
            }
        }


        if (!sapphireNecklaceMatsCheck && !Objects.equals(status, "Selling...")){
            if (grandExchangeArea.contains(myPosition())){
                if (getInventory().contains(sapphireNecklaceUnnotedID) || getInventory().contains(sapphireNecklaceNotedID)){
                    status = "Selling...";
                } else {
                    bank.open();
                    if (bank.isOpen()){
                        if (bank.contains(sapphireNecklaceUnnotedID)){
                            if (bank.getWithdrawMode().equals((Bank.BankMode.WITHDRAW_NOTE))){
                                bank.withdrawAll(sapphireNecklaceUnnotedID);
                            } else {
                                bank.enableMode(Bank.BankMode.WITHDRAW_NOTE);
                            }
                        } else if (!bank.contains(sapphireNecklaceUnnotedID) && !getInventory().contains(sapphireNecklaceUnnotedID) && !bank.contains(sapphireNecklaceNotedID) && !getInventory().contains(sapphireNecklaceNotedID)){
                            status = "Checking materials...";
                            busyWithSapphireNecklaces = false;
                            if (bank.getWithdrawMode().equals((Bank.BankMode.WITHDRAW_ITEM))){
                                busyWithSapphireNecklaces = false;
                                bank.depositAll();
                                sleep(random(1250,2250));
                                //if profit leveling
                                if (profitLeveling){
                                    if (bank.contains("Coins")){
                                        long amountOfCoins = bank.getAmount("Coins");
                                        log("amount of coins in bank..."+amountOfCoins);
                                        if (amountOfCoins >= 275000){
                                            intRequiredGold = 575;
                                            intRequiredSapphire = 575;
                                        } else if (amountOfCoins >= 200000){
                                            intRequiredGold = 450;
                                            intRequiredSapphire = 450;
                                        }else if (amountOfCoins >= 175000){
                                            intRequiredGold = 375;
                                            intRequiredSapphire = 375;
                                        } else if (amountOfCoins >=125000){
                                            intRequiredGold = 275;
                                            intRequiredSapphire = 275;
                                        } else if (amountOfCoins >= 75000){
                                            intRequiredGold = 175;
                                            intRequiredSapphire = 175;
                                        } else if (amountOfCoins >= 45000){
                                            intRequiredGold = 100;
                                            intRequiredSapphire = 100;
                                        } else {
                                            log("Not enough coins");
                                            intRequiredGold = 0;
                                            intRequiredSapphire =0;
                                            stop(false);
                                        }
                                    }
                                } else if (efficientLeveling && bank.contains("Coins")){
                                    long amountOfCoins = bank.getAmount("Coins");
                                    log("amount of coins in bank..."+amountOfCoins);

                                    if (amountOfCoins >= 20000){
                                        intRequiredGold = 26;
                                        intRequiredSapphire = 26;
                                    } else {
                                        log("Not enough coins");
                                        intRequiredGold = 0;
                                        intRequiredSapphire =0;
                                        stop(false);
                                    }
                                }

                                //if efficient leveling

                                if (bank.contains("Gold bar")){
                                    hasGold = true;
                                    log("has gold :"+hasGold);
                                } else {
                                    hasGold = false;
                                    log("has gold :"+hasGold);
                                }
                                if (hasGold){
                                    //if gold bars in bank and inventory is equal to what we need then we have enough gold
                                    if (bank.getItem("Gold bar").getAmount() >= intRequiredGold){
                                        hasEnoughGold = true;
                                        log("has enough gold :"+hasEnoughGold);
                                    } else {
                                        log("has enough gold :"+hasEnoughGold);
                                        long goldAmount = bank.getAmount("Gold bar");
                                        long inventoryGold = getInventory().getAmount(goldBarNotedID)+getInventory().getAmount(goldBarUnnotedID);
                                        long requiredGold = (intRequiredGold-goldAmount-inventoryGold);
                                        intRequiredGold = Math.toIntExact(requiredGold);
                                    }
                                }
                                if (bank.contains("Necklace mould") || getInventory().contains("Necklace mould")){
                                    hasNecklaceMould = true;
                                    log("has necklace mould :"+hasNecklaceMould);
                                } else {
                                    hasNecklaceMould = false;
                                    log("has necklace mould :"+hasNecklaceMould);
                                }
                                if (bank.contains("Sapphire") || getInventory().contains("Sapphire")){
                                    hasSapphire = true;
                                    log("has sapphire :"+hasSapphire);
                                    if (bank.getItem("Sapphire").getAmount() >=intRequiredSapphire){
                                        enoughSapphire = true;
                                        log("enough sapphire :"+enoughSapphire);
                                    } else {
                                        log("enough sapphire :"+enoughSapphire);
                                        long sapphireAmount = bank.getAmount(sapphireUnnotedID);
                                        long inventorySapphire = getInventory().getAmount(sapphireUnnotedID) + getInventory().getAmount(sapphireNotedID);
                                        long requiredSapphire = (intRequiredSapphire-(sapphireAmount-inventorySapphire));
                                        intRequiredSapphire = Math.toIntExact(requiredSapphire);
                                    }
                                } else {
                                    hasSapphire = false;
                                    log("has sapphire :"+hasSapphire);
                                }
                                if (enoughSapphire && hasNecklaceMould && hasEnoughGold  || getInventory().contains(goldBarNotedID) && getInventory().contains(sapphireNotedID) && !getInventory().contains(sapphireNecklaceNotedID)){
                                    status ="Crafting...";
                                } else {
                                    if (!getInventory().contains(sapphireNecklaceNotedID)){
                                        log("Couldnt identify necklaces so not selling any...");
                                        status = "Restocking...";

                                    }
                                    if (getInventory().contains(sapphireNotedID) && getInventory().contains(goldBarNotedID)){
                                        enoughSapphire = true;
                                        log("enough sapphire :"+enoughSapphire);
                                        hasEnoughGold = true;
                                        log("has enough gold :"+hasEnoughGold);
                                        status ="Crafting...";
                                    }

                                }
                                sapphireNecklaceMatsCheck = true;
                                sleep(random(650,1250));
                            } else {
                                bank.enableMode(Bank.BankMode.WITHDRAW_ITEM);
                            }
                        }
                    }
                }

            } else if (edgevilleArea.contains(myPosition())){
                log("checking for materials in edge bank...");
                log("sapphire necklace materials check.:"+sapphireNecklaceMatsCheck);
                if (!sapphireNecklaceMatsCheck){
                    if (edgeBankArea.contains(myPosition())){
                        edgeBank = getObjects().closest("Bank booth");
                        if (edgeBank!= null){
                            bank.open();
                            bank.depositAll();
                            if (bank.getWithdrawMode().equals((Bank.BankMode.WITHDRAW_ITEM))){
                                if (bank.contains(sapphireUnnotedID) && bank.contains(goldBarUnnotedID) && bank.contains("Necklace mould")){
                                    sapphireNecklaceMatsCheck = true;
                                    hasEnoughGold = true;
                                    enoughSapphire = true;
                                    status = "Crafting...";
                                    busyWithSapphireNecklaces = true;
                                } else {
                                    busyWithSapphireNecklaces = false;
                                    log("testing123");
                                    status = "Restocking...";
                                    moveMouseToMinimap();
                                    getWalking().webWalk(Banks.GRAND_EXCHANGE);

                                }
                            } else {
                                bank.enableMode(Bank.BankMode.WITHDRAW_ITEM);
                            }

                        } else {
                            log("cant identify bank booth...");
                        }
                    } else {
                        log("not in edge bank!");
                        moveMouseToMinimap();
                        getWalking().walk(edgeBankArea);

                    }
                }
            }else {
                moveMouseToMinimap();
                getWalking().webWalk(Banks.GRAND_EXCHANGE);

            }
        }

        //if we have verified that we have all the necessary items to craft for this level bracket then we will start crafting
        if (Objects.equals(status,"Crafting...")){
            busyWithSapphireNecklaces = true;
            log("we are ready to craft...");
            if (edgevilleArea.contains(myPosition())){
                log("edge bank111");
                if (getInventory().contains(goldBarNotedID)){
                    bank.open();
                    bank.depositAll(goldBarNotedID);
                }
                if (getInventory().contains(sapphireNotedID)){
                    bank.open();
                    bank.depositAll(sapphireNotedID);
                }
                if (getInventory().contains("Necklace mould") && getInventory().contains("Sapphire") && getInventory().contains("gold bar")){
                    log("All items ready going to craft...");
                    if (getSettings().getRunEnergy() <60){
                        getSettings().setRunning(false);
                    }
                    moveMouseToMinimap();
                    getWalking().webWalk(edgeFurnaceArea);

                    RS2Object edgeFurnace = getObjects().closest("Furnace");
                    if (edgeFurnace != null){
                        edgeFurnace.interact();
                        getMouse().move(random(clientLocationLow.x,clientLocationHigh.x),random(clientLocationLow.y, clientLocationHigh.y));
                        new ConditionalSleep(3500){
                            @Override
                            public boolean condition(){
                                return sapphireNecklaceCraftConfirm() != null;
                            }
                        }.sleep();
                    }
                    if (sapphireNecklaceCraftConfirm() != null){
                        sleep(random(650,1500));
                        sapphireNecklaceCraftConfirm().interact();
                        getMouse().moveOutsideScreen();
                        new ConditionalSleep(25000){
                            @Override
                            public boolean condition(){
                                return !getInventory().contains("Gold bar");
                            }
                        }.sleep();
                        sleep(random(850,3500));
                        getSettings().setRunning(true);
                    }
                } else {
                    log("Couldnt identify resources in inventory...");
                    if (edgeBankArea.contains(myPosition())){
                        log("edge bank222");
                        if (bank.isOpen()){
                            sleep(random(650,1500));
                            if (getInventory().contains(sapphireNecklaceUnnotedID)){
                                bank.deposit(sapphireNecklaceUnnotedID, 13);
                                totalItemsCrafted = 0;
                                bank.depositAll(goldBarUnnotedID);
                                bank.depositAll(sapphireUnnotedID);
                            }
                            if (bank.contains("Necklace mould") && !getInventory().contains("Necklace mould")){
                                bank.withdraw("Necklace mould",1);
                            }
                            if (bank.contains("Sapphire") && !getInventory().contains("Sapphire")){
                                bank.withdraw("Sapphire",13);
                            } else if (!bank.contains("Sapphire")&& !getInventory().contains("Sapphire")){
                                status = "Checking materials...";
                                enoughSapphire = false;
                                sapphireNecklaceMatsCheck = false;
                                status = "Selling...";
                            }
                            if (bank.contains("Gold bar") && !getInventory().contains("Gold bar")){
                                bank.withdraw("Gold bar",13);
                            } else if (!bank.contains("Gold bar")&& !getInventory().contains("Gold bar")){
                                status = "Checking materials...";
                                hasEnoughGold = false;
                                sapphireNecklaceMatsCheck = false;
                                status = "Selling...";
                            }
                            bank.close();
                        } else {
                            bank.open();
                        }
                    } else {
                        moveMouseToMinimap();
                        getWalking().walk(edgeBankArea);

                    }
                }
            } else {
                getSettings().setRunning(true);
                moveMouseToMinimap();
                getWalking().webWalk(edgeBankArea);

            }
        }


        //buys items that we are missing from the ge for crafting
        if (Objects.equals(status,"Restocking...") && sapphireNecklaceMatsCheck && getSkills().getStatic(Skill.CRAFTING) < 40 && profitLeveling ||Objects.equals(status,"Restocking...") && sapphireNecklaceMatsCheck && getSkills().getStatic(Skill.CRAFTING) < 24 && efficientLeveling ){
            if (grandExchangeArea.contains(myPosition())){
                geBank = getObjects().closest(10060);
                NPC geClerk = getNpcs().closest("Grand Exchange Clerk");
                if (geClerk != null){
                    log("ge clerk found");
                    if (geConfirmWidget() == null){
                        geClerk.interact("Exchange");
                        getMouse().move(random(clientLocationLow.x,clientLocationHigh.x),random(clientLocationLow.y, clientLocationHigh.y));
                        new ConditionalSleep(5000){
                            @Override
                            public boolean condition() {
                                return geConfirmWidget()!= null;
                            }
                        }.sleep();
                    }
                    if (geConfirmWidget()!= null){
                        log("Required gold is :"+intRequiredGold);
                        log("Gold in inventory is..."+getInventory().getAmount("Gold bar"));
                        log("Required sapphire is :"+intRequiredSapphire);

                        if (craftedItemValue == 2){
                            craftedItemValue=(getGrandExchange().getOverallPrice(sapphireNecklaceUnnotedID)-((getGrandExchange().getOverallPrice(goldBarUnnotedID)+getGrandExchange().getOverallPrice(sapphireUnnotedID))));
                            log("ge setting profit ..."+craftedItemValue);
                            log("Price of crafted item...:"+getGrandExchange().getOverallPrice(sapphireNecklaceUnnotedID));
                            log("Price of Gold:"+getGrandExchange().getOverallPrice(goldBarUnnotedID));
                            log("Price of sapphire:"+getGrandExchange().getOverallPrice(sapphireUnnotedID));
                        }

                        if (intRequiredGold == 0){
                            hasEnoughGold = true;

                        }
                        if (getInventory().getAmount("Necklace mould") >= 1){
                            hasNecklaceMould = true;
                        }
                        if (intRequiredSapphire == 0){
                            hasSapphire = true;
                        }

                        if (!hasEnoughGold && intRequiredGold!=0){
                            buyFromGE(goldBarUnnotedID,"Gold bar",130,intRequiredGold);
                            sleep(random(850,1850));
                            hasEnoughGold = true;
                        }
                        if (!hasNecklaceMould&& !getInventory().contains("Necklace mould")){
                            buyFromGE(necklaceMouldID,"Necklace mould",800,1);
                            sleep(random(850,1850));
                            hasNecklaceMould = true;
                        }
                        if (!enoughSapphire&& intRequiredSapphire!=0){
                            buyFromGE(sapphireUnnotedID,"Sapphire",260,intRequiredSapphire);
                            sleep(random(850,1850));
                            enoughSapphire = true;
                        }
                        new ConditionalSleep(3500){
                            @Override
                            public boolean condition() {
                                return collectWidget()!= null;
                            }
                        }.sleep();
                        if (collectWidget()!= null){
                            collectWidget().interact();
                            if (hasEnoughGold && enoughSapphire){
                                status = "Crafting...";
                            }
                            log("settingstatustest123");
                            getMouse().move(random(clientLocationLow.x,clientLocationHigh.x),random(clientLocationLow.y, clientLocationHigh.y));
                        }
                    }
                }
            } else {
                moveMouseToMinimap();
                getWalking().webWalk(Banks.GRAND_EXCHANGE);

            }

        }
    }

    public void sapphireAmuletU() throws InterruptedException {

        //NEEDS DUPLICATED
        craftingSapphireAmuletU = true;
        log("sapphire amulet selected...");
        log(status);

        boolean hasGold;
        boolean hasEnoughGold = false;
        boolean hasAmuletMould = false;
        boolean hasSapphire;
        boolean enoughSapphire  = false;
        int intRequiredGold = 0;
        int intRequiredSapphire = 0;
        log("have we check materials?"+sapphireAmuletUMatsCheck);
        log("has enough sapphire..."+enoughSapphire);
        log("has enough gold..."+hasEnoughGold);

        if (Objects.equals(status, "Selling...")){
            busyWithSapphireAmuletU = true;
            if (grandExchangeArea.contains(myPosition())){
                if (inventory.contains(sapphireAmuletUUnnotedID) || inventory.contains(sapphireAmuletUNotedID)){
                    NPC geClerk = getNpcs().closest("Grand Exchange Clerk");
                    if (geClerk != null){
                        if (geConfirmWidget() == null){
                            geClerk.interact("Exchange");
                            getMouse().move(random(clientLocationLow.x,clientLocationHigh.x),random(clientLocationLow.y, clientLocationHigh.y));
                            new ConditionalSleep(5000){
                                @Override
                                public boolean condition() {
                                    return geConfirmWidget()!= null;
                                }
                            }.sleep();
                        }
                        if (geConfirmWidget()!= null){
                            if (collectWidget()!= null && getInventory().contains(sapphireAmuletUNotedID)){
                                collectWidget().interact();
                                status = "checking materials...";
                            }
                            if (getInventory().contains(sapphireAmuletUUnnotedID) ||  inventory.contains(sapphireAmuletUNotedID)){
                                grandExchange.sellItem(sapphireAmuletUNotedID,315,Math.toIntExact(getInventory().getAmount(sapphireAmuletUNotedID)));
                                new ConditionalSleep(350000){
                                    @Override
                                    public boolean condition() {
                                        return collectWidget()!= null;
                                    }
                                }.sleep();
                                if (collectWidget()!= null && !getInventory().contains(sapphireAmuletUNotedID)){
                                    collectWidget().interact();
                                    status = "checking materials...";
                                    busyWithSapphireAmuletU = false;
                                } else {
                                    log("couldnt sell crafted items for gold closing script....");
                                    stop();
                                }
                            } else {
                                log("Couldnt identify or sell sapphire amulets...");
                            }

                        } else {
                            log("Issue with ge widget");
                        }
                    }
                }else {
                    bank.open();
                    if (bank.getWithdrawMode().equals((Bank.BankMode.WITHDRAW_NOTE))){
                        bank.withdrawAll(sapphireAmuletUUnnotedID);
                    } else {
                        bank.enableMode(Bank.BankMode.WITHDRAW_NOTE);
                    }
                }

            } else {
                moveMouseToMinimap();
                getWalking().webWalk(Banks.GRAND_EXCHANGE);
            }
        }


        if (!sapphireAmuletUMatsCheck && !Objects.equals(status, "Selling...")){
            if (grandExchangeArea.contains(myPosition())){
                if (getInventory().contains(sapphireAmuletUUnnotedID) || getInventory().contains(sapphireAmuletUNotedID)){
                    status = "Selling...";
                } else {
                    bank.open();
                    if (bank.isOpen()){
                        if (bank.contains(sapphireAmuletUUnnotedID)){
                            if (bank.getWithdrawMode().equals((Bank.BankMode.WITHDRAW_NOTE))){
                                bank.withdrawAll(sapphireAmuletUUnnotedID);
                            } else {
                                bank.enableMode(Bank.BankMode.WITHDRAW_NOTE);
                            }
                        } else if (!bank.contains(sapphireAmuletUUnnotedID) && !getInventory().contains(sapphireAmuletUUnnotedID) && !bank.contains(sapphireAmuletUNotedID) && !getInventory().contains(sapphireAmuletUNotedID)){
                            status = "Checking materials...";
                            busyWithSapphireAmuletU = false;
                            if (bank.getWithdrawMode().equals((Bank.BankMode.WITHDRAW_ITEM))){
                                busyWithSapphireAmuletU = false;
                                bank.depositAll();
                                sleep(random(1250,2250));
                                //if profit leveling
                                if (profitLeveling){
                                    log("profit leveling doesnt craft sapphireAmulets");
                                } else if (efficientLeveling && bank.contains("Coins")){
                                    long amountOfCoins = bank.getAmount("Coins");
                                    log("amount of coins in bank..."+amountOfCoins);
                                    //122 bars and amulets max
                                    if (amountOfCoins >= 60000){
                                        intRequiredGold = 122;
                                        intRequiredSapphire = 122;
                                    } else if (amountOfCoins >= 30000){
                                        intRequiredGold = 60;
                                        intRequiredSapphire = 60;
                                    }else {
                                        log("Not enough coins");
                                        intRequiredGold = 0;
                                        intRequiredSapphire =0;
                                        stop(false);
                                    }
                                }

                                //if efficient leveling

                                if (bank.contains("Gold bar")){
                                    hasGold = true;
                                    log("has gold :"+hasGold);
                                } else {
                                    hasGold = false;
                                    log("has gold :"+hasGold);
                                }
                                if (hasGold){
                                    //if gold bars in bank and inventory is equal to what we need then we have enough gold
                                    if (bank.getItem("Gold bar").getAmount() >= intRequiredGold){
                                        hasEnoughGold = true;
                                        log("has enough gold :"+hasEnoughGold);
                                    } else {
                                        log("has enough gold :"+hasEnoughGold);
                                        long goldAmount = bank.getAmount("Gold bar");
                                        long inventoryGold = getInventory().getAmount(goldBarNotedID)+getInventory().getAmount(goldBarUnnotedID);
                                        long requiredGold = (intRequiredGold-goldAmount-inventoryGold);
                                        intRequiredGold = Math.toIntExact(requiredGold);
                                    }
                                }
                                if (bank.contains("Amulet mould") || getInventory().contains("Amulet mould")){
                                    hasAmuletMould = true;
                                    log("has amulet mould :"+hasAmuletMould);
                                } else {
                                    hasAmuletMould = false;
                                    log("has amulet mould :"+hasAmuletMould);
                                }
                                if (bank.contains("Sapphire") || getInventory().contains("Sapphire")){
                                    hasSapphire = true;
                                    log("has sapphire :"+hasSapphire);
                                    if (bank.getItem("Sapphire").getAmount() >=intRequiredSapphire){
                                        enoughSapphire = true;
                                        log("enough sapphire :"+enoughSapphire);
                                    } else {
                                        log("enough sapphire :"+enoughSapphire);
                                        long sapphireAmount = bank.getAmount(sapphireUnnotedID);
                                        long inventorySapphire = getInventory().getAmount(sapphireUnnotedID) + getInventory().getAmount(sapphireNotedID);
                                        long requiredSapphire = (intRequiredSapphire-(sapphireAmount-inventorySapphire));
                                        intRequiredSapphire = Math.toIntExact(requiredSapphire);
                                    }
                                } else {
                                    hasSapphire = false;
                                    log("has sapphire :"+hasSapphire);
                                }
                                if (enoughSapphire && hasAmuletMould && hasEnoughGold  || getInventory().contains(goldBarNotedID) && getInventory().contains(sapphireNotedID) && !getInventory().contains(sapphireAmuletUNotedID)){
                                    status ="Crafting...";
                                } else {
                                    if (!getInventory().contains(sapphireAmuletUNotedID)){
                                        log("Couldnt identify amulets so not selling any...");
                                        status = "Restocking...";

                                    }
                                    if (getInventory().contains(sapphireNotedID) && getInventory().contains(goldBarNotedID)){
                                        enoughSapphire = true;
                                        log("enough sapphire :"+enoughSapphire);
                                        hasEnoughGold = true;
                                        log("has enough gold :"+hasEnoughGold);
                                        status ="Crafting...";
                                    }

                                }
                                sapphireAmuletUMatsCheck = true;
                                sleep(random(650,1250));
                            } else {
                                bank.enableMode(Bank.BankMode.WITHDRAW_ITEM);
                            }
                        }
                    }
                }

            } else if (edgevilleArea.contains(myPosition())){
                log("checking for materials in edge bank...");
                log("sapphire amulet materials check.:"+sapphireAmuletUMatsCheck);
                if (!sapphireAmuletUMatsCheck){
                    if (edgeBankArea.contains(myPosition())){
                        edgeBank = getObjects().closest("Bank booth");
                        if (edgeBank!= null){
                            bank.open();
                            bank.depositAll();
                            if (bank.getWithdrawMode().equals((Bank.BankMode.WITHDRAW_ITEM))){
                                if (bank.contains(sapphireUnnotedID) && bank.contains(goldBarUnnotedID) && bank.contains("Amulet mould")){
                                    sapphireAmuletUMatsCheck = true;
                                    hasEnoughGold = true;
                                    enoughSapphire = true;
                                    status = "Crafting...";
                                    busyWithSapphireAmuletU = true;
                                } else {
                                    busyWithSapphireAmuletU = false;
                                    log("testing123");
                                    status = "Restocking...";
                                    moveMouseToMinimap();
                                    getWalking().webWalk(Banks.GRAND_EXCHANGE);

                                }
                            } else {
                                bank.enableMode(Bank.BankMode.WITHDRAW_ITEM);
                            }

                        } else {
                            log("cant identify bank booth...");
                        }
                    } else {
                        log("not in edge bank!");
                        moveMouseToMinimap();
                        getWalking().walk(edgeBankArea);

                    }
                }
            }else {
                moveMouseToMinimap();
                getWalking().webWalk(Banks.GRAND_EXCHANGE);

            }
        }

        //if we have verified that we have all the necessary items to craft for this level bracket then we will start crafting
        if (Objects.equals(status,"Crafting...")){
            busyWithSapphireAmuletU = true;
            log("we are ready to craft...");
            if (edgevilleArea.contains(myPosition())){
                log("edge bank111");
                if (getInventory().contains(goldBarNotedID)){
                    bank.open();
                    bank.depositAll(goldBarNotedID);
                }
                if (getInventory().contains(sapphireNotedID)){
                    bank.open();
                    bank.depositAll(sapphireNotedID);
                }
                if (getInventory().contains("Amulet mould") && getInventory().contains("Sapphire") && getInventory().contains("gold bar")){
                    log("All items ready going to craft...");
                    if (getSettings().getRunEnergy() <60){
                        getSettings().setRunning(false);
                    }
                    moveMouseToMinimap();
                    getWalking().webWalk(edgeFurnaceArea);

                    RS2Object edgeFurnace = getObjects().closest("Furnace");
                    if (edgeFurnace != null){
                        edgeFurnace.interact();
                        getMouse().move(random(clientLocationLow.x,clientLocationHigh.x),random(clientLocationLow.y, clientLocationHigh.y));
                        new ConditionalSleep(3500){
                            @Override
                            public boolean condition(){
                                return sapphireAmuletUCraftConfirm() != null;
                            }
                        }.sleep();
                    }
                    if (sapphireAmuletUCraftConfirm() != null){
                        sleep(random(650,1500));
                        sapphireAmuletUCraftConfirm().interact();
                        getMouse().moveOutsideScreen();
                        new ConditionalSleep(25000){
                            @Override
                            public boolean condition(){
                                return !getInventory().contains("Gold bar");
                            }
                        }.sleep();
                        sleep(random(850,3500));
                        getSettings().setRunning(true);
                    }
                } else {
                    log("Couldnt identify resources in inventory...");
                    if (edgeBankArea.contains(myPosition())){
                        log("edge bank222");
                        if (bank.isOpen()){
                            sleep(random(650,1500));
                            if (getInventory().contains(sapphireAmuletUUnnotedID)){
                                bank.deposit(sapphireAmuletUUnnotedID, 13);
                                totalItemsCrafted = 0;
                                bank.depositAll(goldBarUnnotedID);
                                bank.depositAll(sapphireUnnotedID);
                            }
                            if (bank.contains("Amulet mould") && !getInventory().contains("Amulet mould")){
                                bank.withdraw("Amulet mould",1);
                            }
                            if (bank.contains("Sapphire") && !getInventory().contains("Sapphire")){
                                bank.withdraw("Sapphire",13);
                            } else if (!bank.contains("Sapphire")&& !getInventory().contains("Sapphire")){
                                status = "Checking materials...";
                                enoughSapphire = false;
                                sapphireAmuletUMatsCheck = false;
                                status = "Selling...";
                            }
                            if (bank.contains("Gold bar") && !getInventory().contains("Gold bar")){
                                bank.withdraw("Gold bar",13);
                            } else if (!bank.contains("Gold bar")&& !getInventory().contains("Gold bar")){
                                status = "Checking materials...";
                                hasEnoughGold = false;
                                sapphireAmuletUMatsCheck = false;
                                status = "Selling...";
                            }
                            bank.close();
                        } else {
                            bank.open();
                        }
                    } else {
                        moveMouseToMinimap();
                        getWalking().walk(edgeBankArea);

                    }
                }
            } else {
                getSettings().setRunning(true);
                moveMouseToMinimap();
                getWalking().webWalk(edgeBankArea);

            }
        }


        //buys items that we are missing from the ge for crafting
        if (Objects.equals(status,"Restocking...") && sapphireAmuletUMatsCheck && getSkills().getStatic(Skill.CRAFTING) < 31){
            if (grandExchangeArea.contains(myPosition())){
                geBank = getObjects().closest(10060);
                NPC geClerk = getNpcs().closest("Grand Exchange Clerk");
                if (geClerk != null){
                    log("ge clerk found");
                    if (geConfirmWidget() == null){
                        geClerk.interact("Exchange");
                        getMouse().move(random(clientLocationLow.x,clientLocationHigh.x),random(clientLocationLow.y, clientLocationHigh.y));
                        new ConditionalSleep(5000){
                            @Override
                            public boolean condition() {
                                return geConfirmWidget()!= null;
                            }
                        }.sleep();
                    }
                    if (geConfirmWidget()!= null){
                        log("Required gold is :"+intRequiredGold);
                        log("Gold in inventory is..."+getInventory().getAmount("Gold bar"));
                        log("Required sapphire is :"+intRequiredSapphire);

                        if (craftedItemValue == 2){
                            craftedItemValue=(getGrandExchange().getOverallPrice(sapphireAmuletUUnnotedID)-((getGrandExchange().getOverallPrice(goldBarUnnotedID)+getGrandExchange().getOverallPrice(sapphireUnnotedID))));
                            log("ge setting profit ..."+craftedItemValue);
                            log("Price of crafted item...:"+getGrandExchange().getOverallPrice(sapphireAmuletUUnnotedID));
                            log("Price of Gold:"+getGrandExchange().getOverallPrice(goldBarUnnotedID));
                            log("Price of sapphire:"+getGrandExchange().getOverallPrice(sapphireUnnotedID));
                        }

                        if (intRequiredGold == 0){
                            hasEnoughGold = true;

                        }
                        if (getInventory().getAmount("Amulet mould") >= 1){
                            hasAmuletMould = true;
                        }
                        if (intRequiredSapphire == 0){
                            hasSapphire = true;
                        }

                        if (!hasEnoughGold && intRequiredGold!=0){
                            buyFromGE(goldBarUnnotedID,"Gold bar",130,intRequiredGold);
                            sleep(random(850,1850));
                            hasEnoughGold = true;
                        }
                        if (!hasAmuletMould&& !getInventory().contains("Amulet mould")){
                            buyFromGE(amuletMouldID,"Amulet mould",800,1);
                            sleep(random(850,1850));
                            hasAmuletMould = true;
                        }
                        if (!enoughSapphire&& intRequiredSapphire!=0){
                            buyFromGE(sapphireUnnotedID,"Sapphire",260,intRequiredSapphire);
                            sleep(random(850,1850));
                            enoughSapphire = true;
                        }
                        new ConditionalSleep(3500){
                            @Override
                            public boolean condition() {
                                return collectWidget()!= null;
                            }
                        }.sleep();
                        if (collectWidget()!= null){
                            collectWidget().interact();
                            if (hasEnoughGold && enoughSapphire){
                                status = "Crafting...";
                            }
                            log("settingstatustest123");
                            getMouse().move(random(clientLocationLow.x,clientLocationHigh.x),random(clientLocationLow.y, clientLocationHigh.y));
                        }
                    }
                }
            } else {
                moveMouseToMinimap();
                getWalking().webWalk(Banks.GRAND_EXCHANGE);

            }

        }
    }

    public void emeraldAmuletU() throws InterruptedException {
        //NEEDS DUPLICATED
        craftingEmeraldAmuletU = true;
        log("emerald amulet selected...");
        log(status);

        boolean hasGold;
        boolean hasEnoughGold = false;
        boolean hasAmuletMould = false;
        boolean hasEmerald;
        boolean enoughEmerald  = false;
        int intRequiredGold = 0;
        int intRequiredEmerald = 0;
        log("have we check materials?"+emeraldAmuletUMatsCheck);
        log("has enough emerald..."+enoughEmerald);
        log("has enough gold..."+hasEnoughGold);

        if (Objects.equals(status, "Selling...")){
            busyWithEmeraldAmuletU = true;
            if (grandExchangeArea.contains(myPosition())){
                if (inventory.contains(emeraldAmuletUUnnotedID) || inventory.contains(emeraldAmuletUNotedID)){
                    NPC geClerk = getNpcs().closest("Grand Exchange Clerk");
                    if (geClerk != null){
                        if (geConfirmWidget() == null){
                            geClerk.interact("Exchange");
                            getMouse().move(random(clientLocationLow.x,clientLocationHigh.x),random(clientLocationLow.y, clientLocationHigh.y));
                            new ConditionalSleep(5000){
                                @Override
                                public boolean condition() {
                                    return geConfirmWidget()!= null;
                                }
                            }.sleep();
                        }
                        if (geConfirmWidget()!= null){
                            if (collectWidget()!= null && getInventory().contains(emeraldAmuletUNotedID)){
                                collectWidget().interact();
                                status = "checking materials...";
                            }
                            if (getInventory().contains(emeraldAmuletUUnnotedID) ||  inventory.contains(emeraldAmuletUNotedID)){
                                grandExchange.sellItem(emeraldAmuletUNotedID,620,Math.toIntExact(getInventory().getAmount(emeraldAmuletUNotedID)));
                                new ConditionalSleep(350000){
                                    @Override
                                    public boolean condition() {
                                        return collectWidget()!= null;
                                    }
                                }.sleep();
                                if (collectWidget()!= null && !getInventory().contains(emeraldAmuletUNotedID)){
                                    collectWidget().interact();
                                    status = "checking materials...";
                                    busyWithEmeraldAmuletU = false;
                                } else {
                                    log("couldnt sell crafted items for gold closing script....");
                                    stop();
                                }
                            } else {
                                log("Couldnt identify or sell emerald amulets...");
                            }

                        } else {
                            log("Issue with ge widget");
                        }
                    }
                }else {
                    bank.open();
                    if (bank.getWithdrawMode().equals((Bank.BankMode.WITHDRAW_NOTE))){
                        bank.withdrawAll(emeraldAmuletUUnnotedID);
                    } else {
                        bank.enableMode(Bank.BankMode.WITHDRAW_NOTE);
                    }
                }

            } else {
                moveMouseToMinimap();
                getWalking().webWalk(Banks.GRAND_EXCHANGE);
            }
        }


        if (!emeraldAmuletUMatsCheck && !Objects.equals(status, "Selling...")){
            if (grandExchangeArea.contains(myPosition())){
                if (getInventory().contains(emeraldAmuletUUnnotedID) || getInventory().contains(emeraldAmuletUNotedID)){
                    status = "Selling...";
                } else {
                    bank.open();
                    if (bank.isOpen()){
                        if (bank.contains(emeraldAmuletUUnnotedID)){
                            if (bank.getWithdrawMode().equals((Bank.BankMode.WITHDRAW_NOTE))){
                                bank.withdrawAll(emeraldAmuletUUnnotedID);
                            } else {
                                bank.enableMode(Bank.BankMode.WITHDRAW_NOTE);
                            }
                        } else if (!bank.contains(emeraldAmuletUUnnotedID) && !getInventory().contains(emeraldAmuletUUnnotedID) && !bank.contains(emeraldAmuletUNotedID) && !getInventory().contains(emeraldAmuletUNotedID)){
                            status = "Checking materials...";
                            busyWithEmeraldAmuletU = false;
                            if (bank.getWithdrawMode().equals((Bank.BankMode.WITHDRAW_ITEM))){
                                busyWithEmeraldAmuletU = false;
                                bank.depositAll();
                                sleep(random(1250,2250));
                                //if profit leveling
                                if (profitLeveling){
                                    log("Profit leveling doesnt craft emerald amulets");
                                } else if (efficientLeveling && bank.contains("Coins")){
                                    long amountOfCoins = bank.getAmount("Coins");
                                    log("amount of coins in bank..."+amountOfCoins);
                                    //320 bars and gems
                                    if (amountOfCoins >= 200000){
                                        intRequiredGold = 320;
                                        intRequiredEmerald = 320;
                                    } else if(amountOfCoins >= 115000) {
                                        intRequiredGold = 175;
                                        intRequiredEmerald = 175;
                                    } else if(amountOfCoins >= 60000){
                                        intRequiredGold = 75;
                                        intRequiredEmerald = 75;
                                    }else{
                                        log("Not enough coins");
                                        intRequiredGold = 0;
                                        intRequiredEmerald =0;
                                        stop(false);
                                    }
                                }

                                //if efficient leveling

                                if (bank.contains("Gold bar")){
                                    hasGold = true;
                                    log("has gold :"+hasGold);
                                } else {
                                    hasGold = false;
                                    log("has gold :"+hasGold);
                                }
                                if (hasGold){
                                    //if gold bars in bank and inventory is equal to what we need then we have enough gold
                                    if (bank.getItem("Gold bar").getAmount() >= intRequiredGold){
                                        hasEnoughGold = true;
                                        log("has enough gold :"+hasEnoughGold);
                                    } else {
                                        log("has enough gold :"+hasEnoughGold);
                                        long goldAmount = bank.getAmount("Gold bar");
                                        long inventoryGold = getInventory().getAmount(goldBarNotedID)+getInventory().getAmount(goldBarUnnotedID);
                                        long requiredGold = (intRequiredGold-goldAmount-inventoryGold);
                                        intRequiredGold = Math.toIntExact(requiredGold);
                                    }
                                }
                                if (bank.contains("Amulet mould") || getInventory().contains("Amulet mould")){
                                    hasAmuletMould = true;
                                    log("has amulet mould :"+hasAmuletMould);
                                } else {
                                    hasAmuletMould = false;
                                    log("has amulet mould :"+hasAmuletMould);
                                }
                                if (bank.contains("Emerald") || getInventory().contains("Emerald")){
                                    hasEmerald = true;
                                    log("has emerald :"+hasEmerald);
                                    if (bank.getItem("emerald").getAmount() >=intRequiredEmerald){
                                        enoughEmerald = true;
                                        log("enough emerald :"+enoughEmerald);
                                    } else {
                                        log("enough emerald :"+enoughEmerald);
                                        long emeraldAmount = bank.getAmount(emeraldUnnotedID);
                                        long inventoryEmerald = getInventory().getAmount(emeraldUnnotedID) + getInventory().getAmount(emeraldNotedID);
                                        long requiredEmerald = (intRequiredEmerald-(emeraldAmount-inventoryEmerald));
                                        intRequiredEmerald = Math.toIntExact(requiredEmerald);
                                    }
                                } else {
                                    hasEmerald = false;
                                    log("has emerald :"+hasEmerald);
                                }
                                if (enoughEmerald && hasAmuletMould && hasEnoughGold  || getInventory().contains(goldBarNotedID) && getInventory().contains(emeraldNotedID) && !getInventory().contains(emeraldAmuletUNotedID)){
                                    status ="Crafting...";
                                } else {
                                    if (!getInventory().contains(emeraldAmuletUNotedID)){
                                        log("Couldnt identify amulets so not selling any...");
                                        status = "Restocking...";

                                    }
                                    if (getInventory().contains(emeraldNotedID) && getInventory().contains(goldBarNotedID)){
                                        enoughEmerald = true;
                                        log("enough emerald :"+enoughEmerald);
                                        hasEnoughGold = true;
                                        log("has enough gold :"+hasEnoughGold);
                                        status ="Crafting...";
                                    }

                                }
                                emeraldAmuletUMatsCheck = true;
                                sleep(random(650,1250));
                            } else {
                                bank.enableMode(Bank.BankMode.WITHDRAW_ITEM);
                            }
                        }
                    }
                }

            } else if (edgevilleArea.contains(myPosition())){
                log("checking for materials in edge bank...");
                log("emerald amulet materials check.:"+emeraldAmuletUMatsCheck);
                if (!emeraldAmuletUMatsCheck){
                    if (edgeBankArea.contains(myPosition())){
                        edgeBank = getObjects().closest("Bank booth");
                        if (edgeBank!= null){
                            bank.open();
                            bank.depositAll();
                            if (bank.getWithdrawMode().equals((Bank.BankMode.WITHDRAW_ITEM))){
                                if (bank.contains(emeraldUnnotedID) && bank.contains(goldBarUnnotedID) && bank.contains("Amulet mould")){
                                    emeraldAmuletUMatsCheck = true;
                                    hasEnoughGold = true;
                                    enoughEmerald = true;
                                    status = "Crafting...";
                                    busyWithEmeraldAmuletU = true;
                                } else {
                                    busyWithEmeraldAmuletU = false;
                                    log("testing123");
                                    status = "Restocking...";
                                    moveMouseToMinimap();
                                    getWalking().webWalk(Banks.GRAND_EXCHANGE);

                                }
                            } else {
                                bank.enableMode(Bank.BankMode.WITHDRAW_ITEM);
                            }

                        } else {
                            log("cant identify bank booth...");
                        }
                    } else {
                        log("not in edge bank!");
                        moveMouseToMinimap();
                        getWalking().walk(edgeBankArea);

                    }
                }
            }else {
                moveMouseToMinimap();
                getWalking().webWalk(Banks.GRAND_EXCHANGE);

            }
        }

        //if we have verified that we have all the necessary items to craft for this level bracket then we will start crafting
        if (Objects.equals(status,"Crafting...")){
            busyWithEmeraldAmuletU = true;
            log("we are ready to craft...");
            if (edgevilleArea.contains(myPosition())){
                log("edge bank111");
                if (getInventory().contains(goldBarNotedID)){
                    bank.open();
                    bank.depositAll(goldBarNotedID);
                }
                if (getInventory().contains(emeraldNotedID)){
                    bank.open();
                    bank.depositAll(emeraldNotedID);
                }
                if (getInventory().contains("Amulet mould") && getInventory().contains("emerald") && getInventory().contains("gold bar")){
                    log("All items ready going to craft...");
                    if (getSettings().getRunEnergy() <60){
                        getSettings().setRunning(false);
                    }
                    moveMouseToMinimap();
                    getWalking().webWalk(edgeFurnaceArea);

                    RS2Object edgeFurnace = getObjects().closest("Furnace");
                    if (edgeFurnace != null){
                        edgeFurnace.interact();
                        getMouse().move(random(clientLocationLow.x,clientLocationHigh.x),random(clientLocationLow.y, clientLocationHigh.y));
                        new ConditionalSleep(3500){
                            @Override
                            public boolean condition(){
                                return emeraldAmuletUCraftConfirm() != null;
                            }
                        }.sleep();
                    }
                    if (emeraldAmuletUCraftConfirm() != null){
                        sleep(random(650,1500));
                        emeraldAmuletUCraftConfirm().interact();
                        getMouse().moveOutsideScreen();
                        new ConditionalSleep(25000){
                            @Override
                            public boolean condition(){
                                return !getInventory().contains("Gold bar");
                            }
                        }.sleep();
                        sleep(random(850,3500));
                        getSettings().setRunning(true);
                    }
                } else {
                    log("Couldnt identify resources in inventory...");
                    if (edgeBankArea.contains(myPosition())){
                        log("edge bank222");
                        if (bank.isOpen()){
                            sleep(random(650,1500));
                            if (getInventory().contains(emeraldAmuletUUnnotedID)){
                                bank.deposit(emeraldAmuletUUnnotedID, 13);
                                totalItemsCrafted = 0;
                                bank.depositAll(goldBarUnnotedID);
                                bank.depositAll(emeraldUnnotedID);
                            }
                            if (bank.contains("Amulet mould") && !getInventory().contains("Amulet mould")){
                                bank.withdraw("Amulet mould",1);
                            }
                            if (bank.contains("emerald") && !getInventory().contains("emerald")){
                                bank.withdraw("emerald",13);
                            } else if (!bank.contains("emerald")&& !getInventory().contains("emerald")){
                                status = "Checking materials...";
                                enoughEmerald = false;
                                emeraldAmuletUMatsCheck = false;
                                status = "Selling...";
                            }
                            if (bank.contains("Gold bar") && !getInventory().contains("Gold bar")){
                                bank.withdraw("Gold bar",13);
                            } else if (!bank.contains("Gold bar")&& !getInventory().contains("Gold bar")){
                                status = "Checking materials...";
                                hasEnoughGold = false;
                                emeraldAmuletUMatsCheck = false;
                                status = "Selling...";
                            }
                            bank.close();
                        } else {
                            bank.open();
                        }
                    } else {
                        moveMouseToMinimap();
                        getWalking().walk(edgeBankArea);

                    }
                }
            } else {
                getSettings().setRunning(true);
                moveMouseToMinimap();
                getWalking().webWalk(edgeBankArea);

            }
        }


        //buys items that we are missing from the ge for crafting
        if (Objects.equals(status,"Restocking...") && emeraldAmuletUMatsCheck && getSkills().getStatic(Skill.CRAFTING) < 40){
            if (grandExchangeArea.contains(myPosition())){
                geBank = getObjects().closest(10060);
                NPC geClerk = getNpcs().closest("Grand Exchange Clerk");
                if (geClerk != null){
                    log("ge clerk found");
                    if (geConfirmWidget() == null){
                        geClerk.interact("Exchange");
                        getMouse().move(random(clientLocationLow.x,clientLocationHigh.x),random(clientLocationLow.y, clientLocationHigh.y));
                        new ConditionalSleep(5000){
                            @Override
                            public boolean condition() {
                                return geConfirmWidget()!= null;
                            }
                        }.sleep();
                    }
                    if (geConfirmWidget()!= null){
                        log("Required gold is :"+intRequiredGold);
                        log("Gold in inventory is..."+getInventory().getAmount("Gold bar"));
                        log("Required emerald is :"+intRequiredEmerald);

                        if (craftedItemValue == 2){
                            craftedItemValue=(getGrandExchange().getOverallPrice(emeraldAmuletUUnnotedID)-((getGrandExchange().getOverallPrice(goldBarUnnotedID)+getGrandExchange().getOverallPrice(emeraldUnnotedID))));
                            log("ge setting profit ..."+craftedItemValue);
                            log("Price of crafted item...:"+getGrandExchange().getOverallPrice(emeraldAmuletUUnnotedID));
                            log("Price of Gold:"+getGrandExchange().getOverallPrice(goldBarUnnotedID));
                            log("Price of emerald:"+getGrandExchange().getOverallPrice(emeraldUnnotedID));
                        }

                        if (intRequiredGold == 0){
                            hasEnoughGold = true;

                        }
                        if (getInventory().getAmount("Amulet mould") >= 1){
                            hasAmuletMould = true;
                        }
                        if (intRequiredEmerald == 0){
                            hasEmerald = true;
                        }

                        if (!hasEnoughGold && intRequiredGold!=0){
                            buyFromGE(goldBarUnnotedID,"Gold bar",130,intRequiredGold);
                            sleep(random(850,1850));
                            hasEnoughGold = true;
                        }
                        if (!hasAmuletMould&& !getInventory().contains("Amulet mould")){
                            buyFromGE(amuletMouldID,"Amulet mould",800,1);
                            sleep(random(850,1850));
                            hasAmuletMould = true;
                        }
                        if (!enoughEmerald&& intRequiredEmerald!=0){
                            buyFromGE(emeraldUnnotedID,"emerald",420,intRequiredEmerald);
                            sleep(random(850,1850));
                            enoughEmerald = true;
                        }
                        new ConditionalSleep(3500){
                            @Override
                            public boolean condition() {
                                return collectWidget()!= null;
                            }
                        }.sleep();
                        if (collectWidget()!= null){
                            collectWidget().interact();
                            if (hasEnoughGold && enoughEmerald){
                                status = "Crafting...";
                            }
                            log("settingstatustest123");
                            getMouse().move(random(clientLocationLow.x,clientLocationHigh.x),random(clientLocationLow.y, clientLocationHigh.y));
                        }
                    }
                }
            } else {
                moveMouseToMinimap();
                getWalking().webWalk(Banks.GRAND_EXCHANGE);

            }

        }
    }

    public void diamondRing() throws InterruptedException {

        //NEEDS DUPLICATED
        craftingDiamondRings = true;
        log("diamond ring selected...");
        log(status);

        boolean hasGold;
        boolean hasEnoughGold = false;
        boolean hasRingMould = false;
        boolean hasDiamond;
        boolean enoughDiamond  = false;
        int intRequiredGold = 0;
        int intRequiredDiamond = 0;
        log("have we check materials?"+diamondRingMatsCheck);
        log("has enough diamond..."+enoughDiamond);
        log("has enough gold..."+hasEnoughGold);

        if (Objects.equals(status, "Selling...")){
            busyWithDiamondRings = true;
            if (grandExchangeArea.contains(myPosition())){
                if (inventory.contains(diamondRingUnnotedID) || inventory.contains(diamondRingNotedID)){
                    NPC geClerk = getNpcs().closest("Grand Exchange Clerk");
                    if (geClerk != null){
                        if (geConfirmWidget() == null){
                            geClerk.interact("Exchange");
                            getMouse().move(random(clientLocationLow.x,clientLocationHigh.x),random(clientLocationLow.y, clientLocationHigh.y));
                            new ConditionalSleep(5000){
                                @Override
                                public boolean condition() {
                                    return geConfirmWidget()!= null;
                                }
                            }.sleep();
                        }
                        if (geConfirmWidget()!= null){
                            if (collectWidget()!= null && getInventory().contains(diamondRingNotedID)){
                                collectWidget().interact();
                                status = "checking materials...";
                            }
                            if (getInventory().contains(diamondRingUnnotedID) ||  inventory.contains(diamondRingNotedID)){
                                grandExchange.sellItem(diamondRingNotedID,1860,Math.toIntExact(getInventory().getAmount(diamondRingNotedID)));
                                new ConditionalSleep(350000){
                                    @Override
                                    public boolean condition() {
                                        return collectWidget()!= null;
                                    }
                                }.sleep();
                                if (collectWidget()!= null && !getInventory().contains(diamondRingNotedID)){
                                    collectWidget().interact();
                                    status = "checking materials...";
                                    busyWithDiamondRings = false;
                                } else {
                                    log("couldnt sell crafted items for gold closing script....");
                                    stop();
                                }
                            } else {
                                log("Couldnt identify or sell diamond rings...");
                            }

                        } else {
                            log("Issue with ge widget");
                        }
                    }
                }else {
                    bank.open();
                    if (bank.getWithdrawMode().equals((Bank.BankMode.WITHDRAW_NOTE))){
                        bank.withdrawAll(diamondRingUnnotedID);
                    } else {
                        bank.enableMode(Bank.BankMode.WITHDRAW_NOTE);
                    }
                }

            } else {
                moveMouseToMinimap();
                getWalking().webWalk(Banks.GRAND_EXCHANGE);
            }
        }


        if (!diamondRingMatsCheck && !Objects.equals(status, "Selling...")){
            if (grandExchangeArea.contains(myPosition())){
                if (getInventory().contains(diamondRingUnnotedID) || getInventory().contains(diamondRingNotedID)){
                    status = "Selling...";
                } else {
                    bank.open();
                    if (bank.isOpen()){
                        if (bank.contains(diamondRingUnnotedID)){
                            if (bank.getWithdrawMode().equals((Bank.BankMode.WITHDRAW_NOTE))){
                                bank.withdrawAll(diamondRingUnnotedID);
                            } else {
                                bank.enableMode(Bank.BankMode.WITHDRAW_NOTE);
                            }
                        } else if (!bank.contains(diamondRingUnnotedID) && !getInventory().contains(diamondRingUnnotedID) && !bank.contains(diamondRingNotedID) && !getInventory().contains(diamondRingNotedID)){
                            status = "Checking materials...";
                            busyWithDiamondRings = false;
                            if (bank.getWithdrawMode().equals((Bank.BankMode.WITHDRAW_ITEM))){
                                busyWithDiamondRings = false;
                                bank.depositAll();
                                sleep(random(1250,2250));
                                //if profit leveling

                                //the amount of crafting materials we need to buy based off coins in bank
                                if (profitLeveling){
                                    log("Diamond rings arent crafted with profit leveling");
                                } else if (efficientLeveling && bank.contains("Coins")){
                                    long amountOfCoins = bank.getAmount("Coins");
                                    log("amount of coins in bank..."+amountOfCoins);
                                    //1573 each
                                    if (amountOfCoins >= 3500000){
                                        intRequiredGold = 1573;
                                        intRequiredDiamond = 1573;
                                    } else if(amountOfCoins >= 2300000){
                                        intRequiredGold = 1100;
                                        intRequiredDiamond = 1100;
                                    }else if(amountOfCoins >= 1800000){
                                        intRequiredGold = 750;
                                        intRequiredDiamond = 750;
                                    }else if(amountOfCoins >= 1200000){
                                        intRequiredGold = 500;
                                        intRequiredDiamond = 500;
                                    }else if(amountOfCoins >= 550000){
                                        intRequiredGold = 250;
                                        intRequiredDiamond = 250;
                                    }else if(amountOfCoins >= 230000){
                                        intRequiredGold = 100;
                                        intRequiredDiamond = 100;
                                    }else{
                                        log("Not enough coins");
                                        intRequiredGold = 0;
                                        intRequiredDiamond =0;
                                        stop(false);
                                    }
                                }

                                //if efficient leveling

                                if (bank.contains("Gold bar")){
                                    hasGold = true;
                                    log("has gold :"+hasGold);
                                } else {
                                    hasGold = false;
                                    log("has gold :"+hasGold);
                                }
                                if (hasGold){
                                    //if gold bars in bank and inventory is equal to what we need then we have enough gold
                                    if (bank.getItem("Gold bar").getAmount() >= intRequiredGold){
                                        hasEnoughGold = true;
                                        log("has enough gold :"+hasEnoughGold);
                                    } else {
                                        log("has enough gold :"+hasEnoughGold);
                                        long goldAmount = bank.getAmount("Gold bar");
                                        long inventoryGold = getInventory().getAmount(goldBarNotedID)+getInventory().getAmount(goldBarUnnotedID);
                                        long requiredGold = (intRequiredGold-goldAmount-inventoryGold);
                                        intRequiredGold = Math.toIntExact(requiredGold);
                                    }
                                }
                                if (bank.contains("Ring mould") || getInventory().contains("Ring mould")){
                                    hasRingMould = true;
                                    log("has ring mould :"+hasRingMould);
                                } else {
                                    hasRingMould = false;
                                    log("has ring mould :"+hasRingMould);
                                }
                                if (bank.contains("Diamond") || getInventory().contains("Diamond")){
                                    hasDiamond = true;
                                    log("has diamond :"+hasDiamond);
                                    if (bank.getItem("Diamond").getAmount() >=intRequiredDiamond){
                                        enoughDiamond = true;
                                        log("enough diamond :"+enoughDiamond);
                                    } else {
                                        log("enough diamond :"+enoughDiamond);
                                        long diamondAmount = bank.getAmount(diamondUnnotedID);
                                        long inventoryDiamond = getInventory().getAmount(diamondUnnotedID) + getInventory().getAmount(diamondNotedID);
                                        long requiredDiamond = (intRequiredDiamond-(diamondAmount-inventoryDiamond));
                                        intRequiredDiamond = Math.toIntExact(requiredDiamond);
                                    }
                                } else {
                                    hasDiamond = false;
                                    log("has diamond :"+hasDiamond);
                                }
                                if (enoughDiamond && hasRingMould && hasEnoughGold  || getInventory().contains(goldBarNotedID) && getInventory().contains(diamondNotedID) && !getInventory().contains(diamondRingNotedID)){
                                    status ="Crafting...";
                                } else {
                                    if (!getInventory().contains(diamondRingNotedID)){
                                        log("Couldnt identify rings so not selling any...");
                                        status = "Restocking...";

                                    }
                                    if (getInventory().contains(diamondNotedID) && getInventory().contains(goldBarNotedID)){
                                        enoughDiamond = true;
                                        log("enough diamond :"+enoughDiamond);
                                        hasEnoughGold = true;
                                        log("has enough gold :"+hasEnoughGold);
                                        status ="Crafting...";
                                    }

                                }
                                diamondRingMatsCheck = true;
                                sleep(random(650,1250));
                            } else {
                                bank.enableMode(Bank.BankMode.WITHDRAW_ITEM);
                            }
                        }
                    }
                }

            } else if (edgevilleArea.contains(myPosition())){
                log("checking for materials in edge bank...");
                log("diamond ring materials check.:"+diamondRingMatsCheck);
                if (!diamondRingMatsCheck){
                    if (edgeBankArea.contains(myPosition())){
                        edgeBank = getObjects().closest("Bank booth");
                        if (edgeBank!= null){
                            bank.open();
                            bank.depositAll();
                            if (bank.getWithdrawMode().equals((Bank.BankMode.WITHDRAW_ITEM))){
                                if (bank.contains(diamondUnnotedID) && bank.contains(goldBarUnnotedID) && bank.contains("Ring mould")){
                                    diamondRingMatsCheck = true;
                                    hasEnoughGold = true;
                                    enoughDiamond = true;
                                    status = "Crafting...";
                                    busyWithDiamondRings = true;
                                } else {
                                    busyWithDiamondRings = false;
                                    log("testing123");
                                    status = "Restocking...";
                                    moveMouseToMinimap();
                                    getWalking().webWalk(Banks.GRAND_EXCHANGE);

                                }
                            } else {
                                bank.enableMode(Bank.BankMode.WITHDRAW_ITEM);
                            }

                        } else {
                            log("cant identify bank booth...");
                        }
                    } else {
                        log("not in edge bank!");
                        moveMouseToMinimap();
                        getWalking().walk(edgeBankArea);

                    }
                }
            }else {
                moveMouseToMinimap();
                getWalking().webWalk(Banks.GRAND_EXCHANGE);

            }
        }

        //if we have verified that we have all the necessary items to craft for this level bracket then we will start crafting
        if (Objects.equals(status,"Crafting...")){
            busyWithDiamondRings = true;
            log("we are ready to craft...");
            if (edgevilleArea.contains(myPosition())){
                log("edge bank111");
                if (getInventory().contains(goldBarNotedID)){
                    bank.open();
                    bank.depositAll(goldBarNotedID);
                }
                if (getInventory().contains(diamondNotedID)){
                    bank.open();
                    bank.depositAll(diamondNotedID);
                }
                if (getInventory().contains("Ring mould") && getInventory().contains("Diamond") && getInventory().contains("gold bar")){
                    log("All items ready going to craft...");
                    if (getSettings().getRunEnergy() <60){
                        getSettings().setRunning(false);
                    }
                    moveMouseToMinimap();
                    getWalking().webWalk(edgeFurnaceArea);

                    RS2Object edgeFurnace = getObjects().closest("Furnace");
                    if (edgeFurnace != null){
                        edgeFurnace.interact();
                        getMouse().move(random(clientLocationLow.x,clientLocationHigh.x),random(clientLocationLow.y, clientLocationHigh.y));
                        new ConditionalSleep(3500){
                            @Override
                            public boolean condition(){
                                return diamondRingCraftConfirm() != null;
                            }
                        }.sleep();
                    }
                    if (diamondRingCraftConfirm() != null){
                        sleep(random(650,1500));
                        diamondRingCraftConfirm().interact();
                        getMouse().moveOutsideScreen();
                        new ConditionalSleep(25000){
                            @Override
                            public boolean condition(){
                                return !getInventory().contains("Gold bar");
                            }
                        }.sleep();
                        sleep(random(850,3500));
                        getSettings().setRunning(true);
                    }
                } else {
                    log("Couldnt identify resources in inventory...");
                    if (edgeBankArea.contains(myPosition())){
                        log("edge bank222");
                        if (bank.isOpen()){
                            sleep(random(650,1500));
                            if (getInventory().contains(diamondRingUnnotedID)){
                                bank.deposit(diamondRingUnnotedID, 13);
                                totalItemsCrafted = 0;
                                bank.depositAll(goldBarUnnotedID);
                                bank.depositAll(diamondUnnotedID);
                            }
                            if (bank.contains("Ring mould") && !getInventory().contains("Ring mould")){
                                bank.withdraw("Ring mould",1);
                            }
                            if (bank.contains("Diamond") && !getInventory().contains("Diamond")){
                                bank.withdraw("Diamond",13);
                            } else if (!bank.contains("Diamond")&& !getInventory().contains("Diamond")){
                                status = "Checking materials...";
                                enoughDiamond = false;
                                diamondRingMatsCheck = false;
                                status = "Selling...";
                            }
                            if (bank.contains("Gold bar") && !getInventory().contains("Gold bar")){
                                bank.withdraw("Gold bar",13);
                            } else if (!bank.contains("Gold bar")&& !getInventory().contains("Gold bar")){
                                status = "Checking materials...";
                                hasEnoughGold = false;
                                diamondRingMatsCheck = false;
                                status = "Selling...";
                            }
                            bank.close();
                        } else {
                            bank.open();
                        }
                    } else {
                        moveMouseToMinimap();
                        getWalking().walk(edgeBankArea);

                    }
                }
            } else {
                getSettings().setRunning(true);
                moveMouseToMinimap();
                getWalking().webWalk(edgeBankArea);

            }
        }


        //buys items that we are missing from the ge for crafting
        if (Objects.equals(status,"Restocking...") && diamondRingMatsCheck && getSkills().getStatic(Skill.CRAFTING) < 56){
            if (grandExchangeArea.contains(myPosition())){
                geBank = getObjects().closest(10060);
                NPC geClerk = getNpcs().closest("Grand Exchange Clerk");
                if (geClerk != null){
                    log("ge clerk found");
                    if (geConfirmWidget() == null){
                        geClerk.interact("Exchange");
                        getMouse().move(random(clientLocationLow.x,clientLocationHigh.x),random(clientLocationLow.y, clientLocationHigh.y));
                        new ConditionalSleep(5000){
                            @Override
                            public boolean condition() {
                                return geConfirmWidget()!= null;
                            }
                        }.sleep();
                    }
                    if (geConfirmWidget()!= null){
                        log("Required gold is :"+intRequiredGold);
                        log("Gold in inventory is..."+getInventory().getAmount("Gold bar"));
                        log("Required diamond is :"+intRequiredDiamond);

                        if (craftedItemValue == 2){
                            craftedItemValue=(getGrandExchange().getOverallPrice(diamondRingUnnotedID)-((getGrandExchange().getOverallPrice(goldBarUnnotedID)+getGrandExchange().getOverallPrice(diamondUnnotedID))));
                            log("ge setting profit ..."+craftedItemValue);
                            log("Price of crafted item...:"+getGrandExchange().getOverallPrice(diamondRingUnnotedID));
                            log("Price of Gold:"+getGrandExchange().getOverallPrice(goldBarUnnotedID));
                            log("Price of diamond:"+getGrandExchange().getOverallPrice(diamondUnnotedID));
                        }

                        if (intRequiredGold == 0){
                            hasEnoughGold = true;

                        }
                        if (getInventory().getAmount("Ring mould") >= 1){
                            hasRingMould = true;
                        }
                        if (intRequiredDiamond == 0){
                            hasDiamond = true;
                        }

                        if (!hasEnoughGold && intRequiredGold!=0){
                            buyFromGE(goldBarUnnotedID,"Gold bar",130,intRequiredGold);
                            sleep(random(850,1850));
                            hasEnoughGold = true;
                        }
                        if (!hasRingMould&& !getInventory().contains("Ring mould")){
                            buyFromGE(ringMouldID,"Ring mould",800,1);
                            sleep(random(850,1850));
                            hasRingMould = true;
                        }
                        if (!enoughDiamond&& intRequiredDiamond!=0){
                            buyFromGE(diamondUnnotedID,"Diamond",1800,intRequiredDiamond);
                            sleep(random(850,1850));
                            enoughDiamond = true;
                        }
                        new ConditionalSleep(3500){
                            @Override
                            public boolean condition() {
                                return collectWidget()!= null;
                            }
                        }.sleep();
                        if (collectWidget()!= null){
                            collectWidget().interact();
                            if (hasEnoughGold && enoughDiamond){
                                status = "Crafting...";
                            }
                            log("settingstatustest123");
                            getMouse().move(random(clientLocationLow.x,clientLocationHigh.x),random(clientLocationLow.y, clientLocationHigh.y));
                        }
                    }
                }
            } else {
                moveMouseToMinimap();
                getWalking().webWalk(Banks.GRAND_EXCHANGE);

            }

        }
    }

    public void diamondNecklace() throws InterruptedException {

        //NEEDS DUPLICATED
        craftingDiamondNecklaces = true;
        log("diamond Necklace selected...");
        log(status);

        boolean hasGold;
        boolean hasEnoughGold = false;
        boolean hasNecklaceMould = false;
        boolean hasDiamond;
        boolean enoughDiamond  = false;
        int intRequiredGold = 0;
        int intRequiredDiamond = 0;
        log("have we check materials?"+diamondNecklaceMatsCheck);
        log("has enough diamond..."+enoughDiamond);
        log("has enough gold..."+hasEnoughGold);

        if (Objects.equals(status, "Selling...")){
            busyWithDiamondNecklaces = true;
            if (grandExchangeArea.contains(myPosition())){
                if (inventory.contains(diamondNecklaceUnnotedID) || inventory.contains(diamondNecklaceNotedID)){
                    NPC geClerk = getNpcs().closest("Grand Exchange Clerk");
                    if (geClerk != null){
                        if (geConfirmWidget() == null){
                            geClerk.interact("Exchange");
                            getMouse().move(random(clientLocationLow.x,clientLocationHigh.x),random(clientLocationLow.y, clientLocationHigh.y));
                            new ConditionalSleep(5000){
                                @Override
                                public boolean condition() {
                                    return geConfirmWidget()!= null;
                                }
                            }.sleep();
                        }
                        if (geConfirmWidget()!= null){
                            if (collectWidget()!= null && getInventory().contains(diamondNecklaceNotedID)){
                                collectWidget().interact();
                                status = "checking materials...";
                            }
                            if (getInventory().contains(diamondNecklaceUnnotedID) ||  inventory.contains(diamondNecklaceNotedID)){
                                grandExchange.sellItem(diamondNecklaceNotedID,1960,Math.toIntExact(getInventory().getAmount(diamondNecklaceNotedID)));
                                new ConditionalSleep(350000){
                                    @Override
                                    public boolean condition() {
                                        return collectWidget()!= null;
                                    }
                                }.sleep();
                                if (collectWidget()!= null && !getInventory().contains(diamondNecklaceNotedID)){
                                    collectWidget().interact();
                                    status = "checking materials...";
                                    busyWithDiamondNecklaces = false;
                                } else {
                                    log("couldnt sell crafted items for gold closing script....");
                                    stop();
                                }
                            } else {
                                log("Couldnt identify or sell diamond Necklaces...");
                            }

                        } else {
                            log("Issue with ge widget");
                        }
                    }
                }else {
                    bank.open();
                    if (bank.getWithdrawMode().equals((Bank.BankMode.WITHDRAW_NOTE))){
                        bank.withdrawAll(diamondNecklaceUnnotedID);
                    } else {
                        bank.enableMode(Bank.BankMode.WITHDRAW_NOTE);
                    }
                }

            } else {
                moveMouseToMinimap();
                getWalking().webWalk(Banks.GRAND_EXCHANGE);
            }
        }


        if (!diamondNecklaceMatsCheck && !Objects.equals(status, "Selling...")){
            if (grandExchangeArea.contains(myPosition())){
                if (getInventory().contains(diamondNecklaceUnnotedID) || getInventory().contains(diamondNecklaceNotedID)){
                    status = "Selling...";
                } else {
                    bank.open();
                    if (bank.isOpen()){
                        if (bank.contains(diamondNecklaceUnnotedID)){
                            if (bank.getWithdrawMode().equals((Bank.BankMode.WITHDRAW_NOTE))){
                                bank.withdrawAll(diamondNecklaceUnnotedID);
                            } else {
                                bank.enableMode(Bank.BankMode.WITHDRAW_NOTE);
                            }
                        } else if (!bank.contains(diamondNecklaceUnnotedID) && !getInventory().contains(diamondNecklaceUnnotedID) && !bank.contains(diamondNecklaceNotedID) && !getInventory().contains(diamondNecklaceNotedID)){
                            status = "Checking materials...";
                            busyWithDiamondNecklaces = false;
                            if (bank.getWithdrawMode().equals((Bank.BankMode.WITHDRAW_ITEM))){
                                busyWithDiamondNecklaces = false;
                                bank.depositAll();
                                sleep(random(1250,2250));
                                //if profit leveling


                                if (profitLeveling){
                                    if (bank.contains("Coins")){
                                        long amountOfCoins = bank.getAmount("Coins");
                                        log("amount of coins in bank..."+amountOfCoins);
                                        //1500
                                        if (amountOfCoins >= 3500000){
                                            intRequiredGold = 1573;
                                            intRequiredDiamond = 1573;
                                        } else if(amountOfCoins >= 2300000){
                                            intRequiredGold = 1100;
                                            intRequiredDiamond = 1100;
                                        }else if(amountOfCoins >= 1800000){
                                            intRequiredGold = 750;
                                            intRequiredDiamond = 750;
                                        }else if(amountOfCoins >= 1200000){
                                            intRequiredGold = 500;
                                            intRequiredDiamond = 500;
                                        }else if(amountOfCoins >= 550000){
                                            intRequiredGold = 250;
                                            intRequiredDiamond = 250;
                                        }else if(amountOfCoins >= 230000){
                                            intRequiredGold = 100;
                                            intRequiredDiamond = 100;
                                        }else{
                                            log("Not enough coins");
                                            intRequiredGold = 0;
                                            intRequiredDiamond =0;
                                            stop(false);
                                        }
                                    }
                                } else if (efficientLeveling && bank.contains("Coins")){
                                    long amountOfCoins = bank.getAmount("Coins");
                                    log("amount of coins in bank..."+amountOfCoins);

                                    if (amountOfCoins >= 3500000){
                                        intRequiredGold = 1573;
                                        intRequiredDiamond = 1573;
                                    } else if(amountOfCoins >= 2300000){
                                        intRequiredGold = 1100;
                                        intRequiredDiamond = 1100;
                                    }else if(amountOfCoins >= 1800000){
                                        intRequiredGold = 750;
                                        intRequiredDiamond = 750;
                                    }else if(amountOfCoins >= 1200000){
                                        intRequiredGold = 500;
                                        intRequiredDiamond = 500;
                                    }else if(amountOfCoins >= 550000){
                                        intRequiredGold = 250;
                                        intRequiredDiamond = 250;
                                    }else if(amountOfCoins >= 230000){
                                        intRequiredGold = 100;
                                        intRequiredDiamond = 100;
                                    }else{
                                        log("Not enough coins");
                                        intRequiredGold = 0;
                                        intRequiredDiamond =0;
                                        stop(false);
                                    }
                                }

                                //if efficient leveling

                                if (bank.contains("Gold bar")){
                                    hasGold = true;
                                    log("has gold :"+hasGold);
                                } else {
                                    hasGold = false;
                                    log("has gold :"+hasGold);
                                }
                                if (hasGold){
                                    //if gold bars in bank and inventory is equal to what we need then we have enough gold
                                    if (bank.getItem("Gold bar").getAmount() >= intRequiredGold){
                                        hasEnoughGold = true;
                                        log("has enough gold :"+hasEnoughGold);
                                    } else {
                                        log("has enough gold :"+hasEnoughGold);
                                        long goldAmount = bank.getAmount("Gold bar");
                                        long inventoryGold = getInventory().getAmount(goldBarNotedID)+getInventory().getAmount(goldBarUnnotedID);
                                        long requiredGold = (intRequiredGold-goldAmount-inventoryGold);
                                        intRequiredGold = Math.toIntExact(requiredGold);
                                    }
                                }
                                if (bank.contains("Necklace mould") || getInventory().contains("Necklace mould")){
                                    hasNecklaceMould = true;
                                    log("has Necklace mould :"+hasNecklaceMould);
                                } else {
                                    hasNecklaceMould = false;
                                    log("has Necklace mould :"+hasNecklaceMould);
                                }
                                if (bank.contains("Diamond") || getInventory().contains("Diamond")){
                                    hasDiamond = true;
                                    log("has diamond :"+hasDiamond);
                                    if (bank.getItem("Diamond").getAmount() >=intRequiredDiamond){
                                        enoughDiamond = true;
                                        log("enough diamond :"+enoughDiamond);
                                    } else {
                                        log("enough diamond :"+enoughDiamond);
                                        long diamondAmount = bank.getAmount(diamondUnnotedID);
                                        long inventoryDiamond = getInventory().getAmount(diamondUnnotedID) + getInventory().getAmount(diamondNotedID);
                                        long requiredDiamond = (intRequiredDiamond-(diamondAmount-inventoryDiamond));
                                        intRequiredDiamond = Math.toIntExact(requiredDiamond);
                                    }
                                } else {
                                    hasDiamond = false;
                                    log("has diamond :"+hasDiamond);
                                }
                                if (enoughDiamond && hasNecklaceMould && hasEnoughGold  || getInventory().contains(goldBarNotedID) && getInventory().contains(diamondNotedID) && !getInventory().contains(diamondNecklaceNotedID)){
                                    status ="Crafting...";
                                } else {
                                    if (!getInventory().contains(diamondNecklaceNotedID)){
                                        log("Couldnt identify Necklaces so not selling any...");
                                        status = "Restocking...";

                                    }
                                    if (getInventory().contains(diamondNotedID) && getInventory().contains(goldBarNotedID)){
                                        enoughDiamond = true;
                                        log("enough diamond :"+enoughDiamond);
                                        hasEnoughGold = true;
                                        log("has enough gold :"+hasEnoughGold);
                                        status ="Crafting...";
                                    }

                                }
                                diamondNecklaceMatsCheck = true;
                                sleep(random(650,1250));
                            } else {
                                bank.enableMode(Bank.BankMode.WITHDRAW_ITEM);
                            }
                        }
                    }
                }

            } else if (edgevilleArea.contains(myPosition())){
                log("checking for materials in edge bank...");
                log("diamond Necklace materials check.:"+diamondNecklaceMatsCheck);
                if (!diamondNecklaceMatsCheck){
                    if (edgeBankArea.contains(myPosition())){
                        edgeBank = getObjects().closest("Bank booth");
                        if (edgeBank!= null){
                            bank.open();
                            bank.depositAll();
                            if (bank.getWithdrawMode().equals((Bank.BankMode.WITHDRAW_ITEM))){
                                if (bank.contains(diamondUnnotedID) && bank.contains(goldBarUnnotedID) && bank.contains("Necklace mould")){
                                    diamondNecklaceMatsCheck = true;
                                    hasEnoughGold = true;
                                    enoughDiamond = true;
                                    status = "Crafting...";
                                    busyWithDiamondNecklaces = true;
                                } else {
                                    busyWithDiamondNecklaces = false;
                                    log("testing123");
                                    status = "Restocking...";
                                    moveMouseToMinimap();
                                    getWalking().webWalk(Banks.GRAND_EXCHANGE);

                                }
                            } else {
                                bank.enableMode(Bank.BankMode.WITHDRAW_ITEM);
                            }

                        } else {
                            log("cant identify bank booth...");
                        }
                    } else {
                        log("not in edge bank!");
                        moveMouseToMinimap();
                        getWalking().walk(edgeBankArea);

                    }
                }
            }else {
                moveMouseToMinimap();
                getWalking().webWalk(Banks.GRAND_EXCHANGE);

            }
        }

        //if we have verified that we have all the necessary items to craft for this level bracket then we will start crafting
        if (Objects.equals(status,"Crafting...")){
            busyWithDiamondNecklaces = true;
            log("we are ready to craft...");
            if (edgevilleArea.contains(myPosition())){
                log("edge bank111");
                if (getInventory().contains(goldBarNotedID)){
                    bank.open();
                    bank.depositAll(goldBarNotedID);
                }
                if (getInventory().contains(diamondNotedID)){
                    bank.open();
                    bank.depositAll(diamondNotedID);
                }
                if (getInventory().contains("Necklace mould") && getInventory().contains("Diamond") && getInventory().contains("gold bar")){
                    log("All items ready going to craft...");
                    if (getSettings().getRunEnergy() <60){
                        getSettings().setRunning(false);
                    }
                    moveMouseToMinimap();
                    getWalking().webWalk(edgeFurnaceArea);

                    RS2Object edgeFurnace = getObjects().closest("Furnace");
                    if (edgeFurnace != null){
                        edgeFurnace.interact();
                        getMouse().move(random(clientLocationLow.x,clientLocationHigh.x),random(clientLocationLow.y, clientLocationHigh.y));
                        new ConditionalSleep(3500){
                            @Override
                            public boolean condition(){
                                return diamondNecklaceCraftConfirm() != null;
                            }
                        }.sleep();
                    }
                    if (diamondNecklaceCraftConfirm() != null){
                        sleep(random(650,1500));
                        diamondNecklaceCraftConfirm().interact();
                        getMouse().moveOutsideScreen();
                        new ConditionalSleep(25000){
                            @Override
                            public boolean condition(){
                                return !getInventory().contains("Gold bar");
                            }
                        }.sleep();
                        sleep(random(850,3500));
                        getSettings().setRunning(true);
                    }
                } else {
                    log("Couldnt identify resources in inventory...");
                    if (edgeBankArea.contains(myPosition())){
                        log("edge bank222");
                        if (bank.isOpen()){
                            sleep(random(650,1500));
                            if (getInventory().contains(diamondNecklaceUnnotedID)){
                                bank.deposit(diamondNecklaceUnnotedID, 13);
                                totalItemsCrafted = 0;
                                bank.depositAll(goldBarUnnotedID);
                                bank.depositAll(diamondUnnotedID);
                            }
                            if (bank.contains("Necklace mould") && !getInventory().contains("Necklace mould")){
                                bank.withdraw("Necklace mould",1);
                            }
                            if (bank.contains("Diamond") && !getInventory().contains("Diamond")){
                                bank.withdraw("Diamond",13);
                            } else if (!bank.contains("Diamond")&& !getInventory().contains("Diamond")){
                                status = "Checking materials...";
                                enoughDiamond = false;
                                diamondNecklaceMatsCheck = false;
                                status = "Selling...";
                            }
                            if (bank.contains("Gold bar") && !getInventory().contains("Gold bar")){
                                bank.withdraw("Gold bar",13);
                            } else if (!bank.contains("Gold bar")&& !getInventory().contains("Gold bar")){
                                status = "Checking materials...";
                                hasEnoughGold = false;
                                diamondNecklaceMatsCheck = false;
                                status = "Selling...";
                            }
                            bank.close();
                        } else {
                            bank.open();
                        }
                    } else {
                        moveMouseToMinimap();
                        getWalking().walk(edgeBankArea);

                    }
                }
            } else {
                getSettings().setRunning(true);
                moveMouseToMinimap();
                getWalking().webWalk(edgeBankArea);

            }
        }


        //buys items that we are missing from the ge for crafting
        if (Objects.equals(status,"Restocking...") && diamondNecklaceMatsCheck && getSkills().getStatic(Skill.CRAFTING) < 70 && efficientLeveling ||Objects.equals(status,"Restocking...") && diamondNecklaceMatsCheck && getSkills().getStatic(Skill.CRAFTING) < 99 && profitLeveling){
            if (grandExchangeArea.contains(myPosition())){
                geBank = getObjects().closest(10060);
                NPC geClerk = getNpcs().closest("Grand Exchange Clerk");
                if (geClerk != null){
                    log("ge clerk found");
                    if (geConfirmWidget() == null){
                        geClerk.interact("Exchange");
                        getMouse().move(random(clientLocationLow.x,clientLocationHigh.x),random(clientLocationLow.y, clientLocationHigh.y));
                        new ConditionalSleep(5000){
                            @Override
                            public boolean condition() {
                                return geConfirmWidget()!= null;
                            }
                        }.sleep();
                    }
                    if (geConfirmWidget()!= null){
                        log("Required gold is :"+intRequiredGold);
                        log("Gold in inventory is..."+getInventory().getAmount("Gold bar"));
                        log("Required diamond is :"+intRequiredDiamond);

                        if (craftedItemValue == 2){
                            craftedItemValue=(getGrandExchange().getOverallPrice(diamondNecklaceUnnotedID)-((getGrandExchange().getOverallPrice(goldBarUnnotedID)+getGrandExchange().getOverallPrice(diamondUnnotedID))));
                            log("ge setting profit ..."+craftedItemValue);
                            log("Price of crafted item...:"+getGrandExchange().getOverallPrice(diamondNecklaceUnnotedID));
                            log("Price of Gold:"+getGrandExchange().getOverallPrice(goldBarUnnotedID));
                            log("Price of diamond:"+getGrandExchange().getOverallPrice(diamondUnnotedID));
                        }

                        if (intRequiredGold == 0){
                            hasEnoughGold = true;

                        }
                        if (getInventory().getAmount("Necklace mould") >= 1){
                            hasNecklaceMould = true;
                        }
                        if (intRequiredDiamond == 0){
                            hasDiamond = true;
                        }

                        if (!hasEnoughGold && intRequiredGold!=0){
                            buyFromGE(goldBarUnnotedID,"Gold bar",130,intRequiredGold);
                            sleep(random(850,1850));
                            hasEnoughGold = true;
                        }
                        if (!hasNecklaceMould&& !getInventory().contains("Necklace mould")){
                            buyFromGE(necklaceMouldID,"Necklace mould",800,1);
                            sleep(random(850,1850));
                            hasNecklaceMould = true;
                        }
                        if (!enoughDiamond&& intRequiredDiamond!=0){
                            buyFromGE(diamondUnnotedID,"Diamond",1800,intRequiredDiamond);
                            sleep(random(850,1850));
                            enoughDiamond = true;
                        }
                        new ConditionalSleep(3500){
                            @Override
                            public boolean condition() {
                                return collectWidget()!= null;
                            }
                        }.sleep();
                        if (collectWidget()!= null){
                            collectWidget().interact();
                            if (hasEnoughGold && enoughDiamond){
                                status = "Crafting...";
                            }
                            log("settingstatustest123");
                            getMouse().move(random(clientLocationLow.x,clientLocationHigh.x),random(clientLocationLow.y, clientLocationHigh.y));
                        }
                    }
                }
            } else {
                moveMouseToMinimap();
                getWalking().webWalk(Banks.GRAND_EXCHANGE);

            }

        }
    }

    public void diamondAmuletU() throws InterruptedException {

        //NEEDS DUPLICATED
        craftingDiamondAmuletU = true;
        log("diamond Amulets selected...");
        log(status);

        boolean hasGold;
        boolean hasEnoughGold = false;
        boolean hasAmuletMould = false;
        boolean hasDiamond;
        boolean enoughDiamond  = false;
        int intRequiredGold = 0;
        int intRequiredDiamond = 0;
        log("have we check materials?"+diamondAmuletUMatsCheck);
        log("has enough diamond..."+enoughDiamond);
        log("has enough gold..."+hasEnoughGold);

        if (Objects.equals(status, "Selling...")){
            busyWithDiamondAmuletU = true;
            if (grandExchangeArea.contains(myPosition())){
                if (inventory.contains(diamondAmuletUUnnotedID) || inventory.contains(diamondAmuletUNotedID)){
                    NPC geClerk = getNpcs().closest("Grand Exchange Clerk");
                    if (geClerk != null){
                        if (geConfirmWidget() == null){
                            geClerk.interact("Exchange");
                            getMouse().move(random(clientLocationLow.x,clientLocationHigh.x),random(clientLocationLow.y, clientLocationHigh.y));
                            new ConditionalSleep(5000){
                                @Override
                                public boolean condition() {
                                    return geConfirmWidget()!= null;
                                }
                            }.sleep();
                        }
                        if (geConfirmWidget()!= null){
                            if (collectWidget()!= null && getInventory().contains(diamondAmuletUNotedID)){
                                collectWidget().interact();
                                status = "checking materials...";
                            }
                            if (getInventory().contains(diamondAmuletUUnnotedID) ||  inventory.contains(diamondAmuletUNotedID)){
                                grandExchange.sellItem(diamondAmuletUNotedID,1850,Math.toIntExact(getInventory().getAmount(diamondAmuletUNotedID)));
                                new ConditionalSleep(350000){
                                    @Override
                                    public boolean condition() {
                                        return collectWidget()!= null;
                                    }
                                }.sleep();
                                if (collectWidget()!= null && !getInventory().contains(diamondAmuletUNotedID)){
                                    collectWidget().interact();
                                    status = "checking materials...";
                                    busyWithDiamondAmuletU = false;
                                } else {
                                    log("couldnt sell crafted items for gold closing script....");
                                    stop();
                                }
                            } else {
                                log("Couldnt identify or sell diamond AmuletU...");
                            }

                        } else {
                            log("Issue with ge widget");
                        }
                    }
                }else {
                    bank.open();
                    if (bank.getWithdrawMode().equals((Bank.BankMode.WITHDRAW_NOTE))){
                        bank.withdrawAll(diamondAmuletUUnnotedID);
                    } else {
                        bank.enableMode(Bank.BankMode.WITHDRAW_NOTE);
                    }
                }

            } else {
                moveMouseToMinimap();
                getWalking().webWalk(Banks.GRAND_EXCHANGE);
            }
        }


        if (!diamondAmuletUMatsCheck && !Objects.equals(status, "Selling...")){
            if (grandExchangeArea.contains(myPosition())){
                if (getInventory().contains(diamondAmuletUUnnotedID) || getInventory().contains(diamondAmuletUNotedID)){
                    status = "Selling...";
                } else {
                    bank.open();
                    if (bank.isOpen()){
                        if (bank.contains(diamondAmuletUUnnotedID)){
                            if (bank.getWithdrawMode().equals((Bank.BankMode.WITHDRAW_NOTE))){
                                bank.withdrawAll(diamondAmuletUUnnotedID);
                            } else {
                                bank.enableMode(Bank.BankMode.WITHDRAW_NOTE);
                            }
                        } else if (!bank.contains(diamondAmuletUUnnotedID) && !getInventory().contains(diamondAmuletUUnnotedID) && !bank.contains(diamondAmuletUNotedID) && !getInventory().contains(diamondAmuletUNotedID)){
                            status = "Checking materials...";
                            busyWithDiamondAmuletU = false;
                            if (bank.getWithdrawMode().equals((Bank.BankMode.WITHDRAW_ITEM))){
                                busyWithDiamondAmuletU = false;
                                bank.depositAll();
                                sleep(random(1250,2250));
                                //if profit leveling

                                if (profitLeveling){
                                    log("Profit leveling doesnt craft amulets");
                                } else if (efficientLeveling && bank.contains("Coins")){
                                    long amountOfCoins = bank.getAmount("Coins");
                                    log("amount of coins in bank..."+amountOfCoins);

                                    if (amountOfCoins >= 3500000){
                                        intRequiredGold = 1573;
                                        intRequiredDiamond = 1573;
                                    } else if(amountOfCoins >= 2300000){
                                        intRequiredGold = 1100;
                                        intRequiredDiamond = 1100;
                                    }else if(amountOfCoins >= 1800000){
                                        intRequiredGold = 750;
                                        intRequiredDiamond = 750;
                                    }else if(amountOfCoins >= 1200000){
                                        intRequiredGold = 500;
                                        intRequiredDiamond = 500;
                                    }else if(amountOfCoins >= 550000){
                                        intRequiredGold = 250;
                                        intRequiredDiamond = 250;
                                    }else if(amountOfCoins >= 230000){
                                        intRequiredGold = 100;
                                        intRequiredDiamond = 100;
                                    }else{
                                        log("Not enough coins");
                                        intRequiredGold = 0;
                                        intRequiredDiamond =0;
                                        stop(false);
                                    }
                                }

                                //if efficient leveling

                                if (bank.contains("Gold bar")){
                                    hasGold = true;
                                    log("has gold :"+hasGold);
                                } else {
                                    hasGold = false;
                                    log("has gold :"+hasGold);
                                }
                                if (hasGold){
                                    //if gold bars in bank and inventory is equal to what we need then we have enough gold
                                    if (bank.getItem("Gold bar").getAmount() >= intRequiredGold){
                                        hasEnoughGold = true;
                                        log("has enough gold :"+hasEnoughGold);
                                    } else {
                                        log("has enough gold :"+hasEnoughGold);
                                        long goldAmount = bank.getAmount("Gold bar");
                                        long inventoryGold = getInventory().getAmount(goldBarNotedID)+getInventory().getAmount(goldBarUnnotedID);
                                        long requiredGold = (intRequiredGold-goldAmount-inventoryGold);
                                        intRequiredGold = Math.toIntExact(requiredGold);
                                    }
                                }
                                if (bank.contains("Amulet mould") || getInventory().contains("Amulet mould")){
                                    hasAmuletMould = true;
                                    log("has Amulet mould :"+hasAmuletMould);
                                } else {
                                    hasAmuletMould = false;
                                    log("has Amulet mould :"+hasAmuletMould);
                                }
                                if (bank.contains("Diamond") || getInventory().contains("Diamond")){
                                    hasDiamond = true;
                                    log("has diamond :"+hasDiamond);
                                    if (bank.getItem("Diamond").getAmount() >=intRequiredDiamond){
                                        enoughDiamond = true;
                                        log("enough diamond :"+enoughDiamond);
                                    } else {
                                        log("enough diamond :"+enoughDiamond);
                                        long diamondAmount = bank.getAmount(diamondUnnotedID);
                                        long inventoryDiamond = getInventory().getAmount(diamondUnnotedID) + getInventory().getAmount(diamondNotedID);
                                        long requiredDiamond = (intRequiredDiamond-(diamondAmount-inventoryDiamond));
                                        intRequiredDiamond = Math.toIntExact(requiredDiamond);
                                    }
                                } else {
                                    hasDiamond = false;
                                    log("has diamond :"+hasDiamond);
                                }
                                if (enoughDiamond && hasAmuletMould && hasEnoughGold  || getInventory().contains(goldBarNotedID) && getInventory().contains(diamondNotedID) && !getInventory().contains(diamondAmuletUNotedID)){
                                    status ="Crafting...";
                                } else {
                                    if (!getInventory().contains(diamondAmuletUNotedID)){
                                        log("Couldnt identify AmuletU so not selling any...");
                                        status = "Restocking...";

                                    }
                                    if (getInventory().contains(diamondNotedID) && getInventory().contains(goldBarNotedID)){
                                        enoughDiamond = true;
                                        log("enough diamond :"+enoughDiamond);
                                        hasEnoughGold = true;
                                        log("has enough gold :"+hasEnoughGold);
                                        status ="Crafting...";
                                    }

                                }
                                diamondAmuletUMatsCheck = true;
                                sleep(random(650,1250));
                            } else {
                                bank.enableMode(Bank.BankMode.WITHDRAW_ITEM);
                            }
                        }
                    }
                }

            } else if (edgevilleArea.contains(myPosition())){
                log("checking for materials in edge bank...");
                log("diamond Amulets materials check.:"+diamondAmuletUMatsCheck);
                if (!diamondAmuletUMatsCheck){
                    if (edgeBankArea.contains(myPosition())){
                        edgeBank = getObjects().closest("Bank booth");
                        if (edgeBank!= null){
                            bank.open();
                            bank.depositAll();
                            if (bank.getWithdrawMode().equals((Bank.BankMode.WITHDRAW_ITEM))){
                                if (bank.contains(diamondUnnotedID) && bank.contains(goldBarUnnotedID) && bank.contains("Amulet mould")){
                                    diamondAmuletUMatsCheck = true;
                                    hasEnoughGold = true;
                                    enoughDiamond = true;
                                    status = "Crafting...";
                                    busyWithDiamondAmuletU = true;
                                } else {
                                    busyWithDiamondAmuletU = false;
                                    log("testing123");
                                    status = "Restocking...";
                                    moveMouseToMinimap();
                                    getWalking().webWalk(Banks.GRAND_EXCHANGE);

                                }
                            } else {
                                bank.enableMode(Bank.BankMode.WITHDRAW_ITEM);
                            }

                        } else {
                            log("cant identify bank booth...");
                        }
                    } else {
                        log("not in edge bank!");
                        moveMouseToMinimap();
                        getWalking().walk(edgeBankArea);

                    }
                }
            }else {
                moveMouseToMinimap();
                getWalking().webWalk(Banks.GRAND_EXCHANGE);

            }
        }

        //if we have verified that we have all the necessary items to craft for this level bracket then we will start crafting
        if (Objects.equals(status,"Crafting...")){
            busyWithDiamondAmuletU = true;
            log("we are ready to craft...");
            if (edgevilleArea.contains(myPosition())){
                log("edge bank111");
                if (getInventory().contains(goldBarNotedID)){
                    bank.open();
                    bank.depositAll(goldBarNotedID);
                }
                if (getInventory().contains(diamondNotedID)){
                    bank.open();
                    bank.depositAll(diamondNotedID);
                }
                if (getInventory().contains("Amulet mould") && getInventory().contains("Diamond") && getInventory().contains("gold bar")){
                    log("All items ready going to craft...");
                    if (getSettings().getRunEnergy() <60){
                        getSettings().setRunning(false);
                    }
                    moveMouseToMinimap();
                    getWalking().webWalk(edgeFurnaceArea);

                    RS2Object edgeFurnace = getObjects().closest("Furnace");
                    if (edgeFurnace != null){
                        edgeFurnace.interact();
                        getMouse().move(random(clientLocationLow.x,clientLocationHigh.x),random(clientLocationLow.y, clientLocationHigh.y));
                        new ConditionalSleep(3500){
                            @Override
                            public boolean condition(){
                                return diamondAmuletUCraftConfirm() != null;
                            }
                        }.sleep();
                    }
                    if (diamondAmuletUCraftConfirm() != null){
                        sleep(random(650,1500));
                        diamondAmuletUCraftConfirm().interact();
                        getMouse().moveOutsideScreen();
                        new ConditionalSleep(25000){
                            @Override
                            public boolean condition(){
                                return !getInventory().contains("Gold bar");
                            }
                        }.sleep();
                        sleep(random(850,3500));
                        getSettings().setRunning(true);
                    }
                } else {
                    log("Couldnt identify resources in inventory...");
                    if (edgeBankArea.contains(myPosition())){
                        log("edge bank222");
                        if (bank.isOpen()){
                            sleep(random(650,1500));
                            if (getInventory().contains(diamondAmuletUUnnotedID)){
                                bank.deposit(diamondAmuletUUnnotedID, 13);
                                totalItemsCrafted = 0;
                                bank.depositAll(goldBarUnnotedID);
                                bank.depositAll(diamondUnnotedID);
                            }
                            if (bank.contains("Amulet mould") && !getInventory().contains("Amulet mould")){
                                bank.withdraw("Amulet mould",1);
                            }
                            if (bank.contains("Diamond") && !getInventory().contains("Diamond")){
                                bank.withdraw("Diamond",13);
                            } else if (!bank.contains("Diamond")&& !getInventory().contains("Diamond")){
                                status = "Checking materials...";
                                enoughDiamond = false;
                                diamondAmuletUMatsCheck = false;
                                status = "Selling...";
                            }
                            if (bank.contains("Gold bar") && !getInventory().contains("Gold bar")){
                                bank.withdraw("Gold bar",13);
                            } else if (!bank.contains("Gold bar")&& !getInventory().contains("Gold bar")){
                                status = "Checking materials...";
                                hasEnoughGold = false;
                                diamondAmuletUMatsCheck = false;
                                status = "Selling...";
                            }
                            bank.close();
                        } else {
                            bank.open();
                        }
                    } else {
                        moveMouseToMinimap();
                        getWalking().walk(edgeBankArea);

                    }
                }
            } else {
                getSettings().setRunning(true);
                moveMouseToMinimap();
                getWalking().webWalk(edgeBankArea);

            }
        }


        //buys items that we are missing from the ge for crafting
        if (Objects.equals(status,"Restocking...") && diamondAmuletUMatsCheck && getSkills().getStatic(Skill.CRAFTING) < 99){
            if (grandExchangeArea.contains(myPosition())){
                geBank = getObjects().closest(10060);
                NPC geClerk = getNpcs().closest("Grand Exchange Clerk");
                if (geClerk != null){
                    log("ge clerk found");
                    if (geConfirmWidget() == null){
                        geClerk.interact("Exchange");
                        getMouse().move(random(clientLocationLow.x,clientLocationHigh.x),random(clientLocationLow.y, clientLocationHigh.y));
                        new ConditionalSleep(5000){
                            @Override
                            public boolean condition() {
                                return geConfirmWidget()!= null;
                            }
                        }.sleep();
                    }
                    if (geConfirmWidget()!= null){
                        log("Required gold is :"+intRequiredGold);
                        log("Gold in inventory is..."+getInventory().getAmount("Gold bar"));
                        log("Required diamond is :"+intRequiredDiamond);

                        if (craftedItemValue == 2){
                            craftedItemValue=(getGrandExchange().getOverallPrice(diamondAmuletUUnnotedID)-((getGrandExchange().getOverallPrice(goldBarUnnotedID)+getGrandExchange().getOverallPrice(diamondUnnotedID))));
                            log("ge setting profit ..."+craftedItemValue);
                            log("Price of crafted item...:"+getGrandExchange().getOverallPrice(diamondAmuletUUnnotedID));
                            log("Price of Gold:"+getGrandExchange().getOverallPrice(goldBarUnnotedID));
                            log("Price of diamond:"+getGrandExchange().getOverallPrice(diamondUnnotedID));
                        }

                        if (intRequiredGold == 0){
                            hasEnoughGold = true;

                        }
                        if (getInventory().getAmount("Amulet mould") >= 1){
                            hasAmuletMould = true;
                        }
                        if (intRequiredDiamond == 0){
                            hasDiamond = true;
                        }

                        if (!hasEnoughGold && intRequiredGold!=0){
                            buyFromGE(goldBarUnnotedID,"Gold bar",130,intRequiredGold);
                            sleep(random(850,1850));
                            hasEnoughGold = true;
                        }
                        if (!hasAmuletMould&& !getInventory().contains("Amulet mould")){
                            buyFromGE(amuletMouldID,"Amulet mould",800,1);
                            sleep(random(850,1850));
                            hasAmuletMould = true;
                        }
                        if (!enoughDiamond&& intRequiredDiamond!=0){
                            buyFromGE(diamondUnnotedID,"Diamond",1800,intRequiredDiamond);
                            sleep(random(850,1850));
                            enoughDiamond = true;
                        }
                        new ConditionalSleep(3500){
                            @Override
                            public boolean condition() {
                                return collectWidget()!= null;
                            }
                        }.sleep();
                        if (collectWidget()!= null){
                            collectWidget().interact();
                            if (hasEnoughGold && enoughDiamond){
                                status = "Crafting...";
                            }
                            log("settingstatustest123");
                            getMouse().move(random(clientLocationLow.x,clientLocationHigh.x),random(clientLocationLow.y, clientLocationHigh.y));
                        }
                    }
                }
            } else {
                moveMouseToMinimap();
                getWalking().webWalk(Banks.GRAND_EXCHANGE);

            }

        }
    }


    @Override
    public int onLoop() throws InterruptedException {

        if (profitLeveling){
            setCameraZoom();
            log(status);
            if (getSkills().getStatic(Skill.CRAFTING) >= 6) {
                if (getSkills().getStatic(Skill.CRAFTING) >= 22 && !busyWithGoldNecklaces){
                    if (getSkills().getStatic(Skill.CRAFTING) >= 40 && !busyWithSapphireNecklaces) {
                        if (getSkills().getStatic(Skill.CRAFTING) >= 58 && !busyWithRubyNecklaces) {//should be 56
                            log("ready to craft diamond necklaces...");
                            craftingRubyNecklaces = false;
                            diamondNecklace();
                        } else {
                            rubyNecklace();
                        }
                    } else {
                        sapphireNecklace();
                    }
                } else {
                    goldNecklace();
                }
            } else {
                leatherGloves();
            }
        } else if (efficientLeveling){
            setCameraZoom();
            if (getSkills().getStatic(Skill.CRAFTING) >= 6) {
                if (getSkills().getStatic(Skill.CRAFTING) >= 8 && !busyWithGoldNecklaces){
                    if (getSkills().getStatic(Skill.CRAFTING) >= 22 && !busyWithGoldAmuletU) {
                        if (getSkills().getStatic(Skill.CRAFTING) >= 24 && !busyWithSapphireNecklaces) {
                            if (getSkills().getStatic(Skill.CRAFTING) >= 31 && !busyWithSapphireAmuletU) {
                                if (getSkills().getStatic(Skill.CRAFTING) >= 40 && !busyWithEmeraldAmuletU) {
                                    if (getSkills().getStatic(Skill.CRAFTING) >= 43 && !busyWithRubyNecklaces) {
                                        if (getSkills().getStatic(Skill.CRAFTING) >= 56 && !busyWithDiamondRings) {//should be 56 but extending level requirement for testing
                                            if (getSkills().getStatic(Skill.CRAFTING) >= 70 && !busyWithDiamondNecklaces) {
                                                diamondAmuletU();
                                            } else {
                                                diamondNecklace();
                                            }
                                        } else {
                                            diamondRing();
                                        }
                                    } else {
                                        rubyNecklace();
                                    }
                                } else {
                                    emeraldAmuletU();
                                }
                            } else {
                                sapphireAmuletU();
                            }
                        } else {
                            sapphireNecklace();
                        }
                    } else {
                        goldAmuletU();
                    }
                } else {
                    goldNecklace();
                }
            } else {
                leatherGloves();
            }
        } else {
            log("cant identify selected leveling method...");
        }

        return random(200, 300);
    }

    private void setCameraZoom() {
        if (!ZoomControl.isInRange(getCamera().getScaleZ(), 275)) {
            ZoomControl.setZoom(getBot(), 275);
            getMouse().move(577,316);
            if (inventoryWidgetCheck() != null){
                inventoryWidgetCheck().interact();
                getMouse().move(random(clientLocationLow.x,clientLocationHigh.x),random(clientLocationLow.y, clientLocationHigh.y));
            }
        }
    }

    @Override
    public void onExit() {

    }

    public final String formatTime(final long ms){
        long s = ms / 1000, m = s / 60, h = m / 60;
        s %= 60; m %= 60; h %= 24;
        return String.format("%02d:%02d:%02d", h, m, s);
    }
    public int getPerHour(int value) {
        return (int) (value * 3600000d / (System.currentTimeMillis() - startTime));
    }
    private final LinkedList<MousePathPoint> mousePath = new LinkedList<MousePathPoint>();
    private final LinkedList<MousePathPoint2> mousePath2 = new LinkedList<MousePathPoint2>();

    class MousePathPoint extends Point {

        private long finishTime;
        private double lastingTime;

        public MousePathPoint(int x, int y, int lastingTime) {
            super(x, y);
            this.lastingTime = lastingTime;
            finishTime = System.currentTimeMillis() + lastingTime;
        }

        public boolean isUp() {
            return System.currentTimeMillis() > finishTime;
        }

    }

    class MousePathPoint2 extends Point {

        private long finishTime;
        private double lastingTime;

        public MousePathPoint2(int x, int y, int lastingTime) {
            super(x, y);
            this.lastingTime = lastingTime;
            finishTime = System.currentTimeMillis() + lastingTime;

        }

        public boolean isUp() {
            return System.currentTimeMillis() > finishTime;
        }

    }
//rubyNecklaceUnnotedID ruby neclace id
    @Override
    public void onPaint(Graphics2D g) {



        for (Item i : getInventory().getItems()) {
            totalValue += valueOfItem(i);
        }


        final long runTime = System.currentTimeMillis() - startTime;
        if (newImage != null) {
            g.drawImage(newImage, null, 0, 95);
        }
        Point mP = getMouse().getPosition();

        g.setColor(Color.cyan);
        g.drawLine(mP.x, 0, mP.x, 500);
// Draw a line from left of screen (0), to right (800), with mouse y coordinate
        g.drawLine(0, mP.y, 800, mP.y);

        Font font = new Font("Impact", Font.PLAIN, 20);
        g.setFont(font);

        while (!mousePath.isEmpty() && mousePath.peek().isUp())
            mousePath.remove();
        Point clientCursor = getMouse().getPosition();
        MousePathPoint mpp = new MousePathPoint(clientCursor.x, clientCursor.y, 300); //1000 = lasting time/MS
        if (mousePath.isEmpty() || !mousePath.getLast().equals(mpp))
            mousePath.add(mpp);
        MousePathPoint lastPoint = null;
        for (MousePathPoint a : mousePath) {
            if (lastPoint != null) {
                g.setColor(Color.MAGENTA); //trail color
                g.drawLine(a.x, a.y, lastPoint.x, lastPoint.y);
            }
            lastPoint = a;
        }
        //+15+15 is bottom right
        //-15-15 is top left
        //+15-15 is top right
        //0-15 should be top middle
        //-15+15 is bottom left
        while (!mousePath2.isEmpty() && mousePath2.peek().isUp())
            mousePath2.remove();
        Point clientCursor2 = getMouse().getPosition();
        MousePathPoint2 mpp2 = new MousePathPoint2(clientCursor2.x+5, clientCursor2.y-5, 75); //1000 = lasting time/MS
        if (mousePath2.isEmpty() || !mousePath2.getLast().equals(mpp))
            mousePath2.add(mpp2);
        MousePathPoint2 lastPoint2 = null;
        for (MousePathPoint2 a : mousePath2) {
            if (lastPoint2 != null) {
                g.setColor(Color.cyan); //trail color
                g.drawLine(a.x, a.y, lastPoint2.x, lastPoint2.y);
            }
            lastPoint2 = a;
        }

        g.setColor(Color.white);
        for(final Skill skill : new Skill[]{Skill.CRAFTING}) {
            getExperienceTracker().start(skill);
            int currentCraftingEXP = getSkills().getExperience(skill);
            int totalCraftingEXPGained = currentCraftingEXP -startingCraftingEXP;
            int CraftingEXPPerHour = getPerHour(totalCraftingEXPGained);
            g.drawString("Status is :" + status,9,310);
            g.drawString("runtime is :"+formatTime(runTime), 9,335);
            g.drawString("Crafting level :" + getSkills().getStatic(Skill.CRAFTING),9,260);
            g.setColor(Color.GREEN);
            g.drawString("EXP per hour :"+CraftingEXPPerHour, 185, 310);
            g.drawString("EXP gained :"+(currentCraftingEXP-startingCraftingEXP), 185, 335);
            g.setColor(Color.ORANGE);
            g.drawString("Money made :" + totalValue,185,285);
            g.drawString("Hourly profit:"+getPerHour(totalValue), 185,260);


        }
    }
//totalValue < totalItemsCrafted * craftedItemValue

    private int valueOfItem(Item i) {
        //NEEDS DUPLICATED

        if (craftingGoldNecklaces){
            int craftedItemsInInventory = Math.toIntExact(getInventory().getAmount(goldNecklaceUnnotedID));
            boolean profitAdded = true;
            if (profitAdded && craftedItemsInInventory > totalItemsCrafted){
                totalItemsCrafted = totalItemsCrafted +1;
                profitAdded = false;
            }
            if (getInventory().contains(goldNecklaceUnnotedID) && !profitAdded|| totalValue == 0 && getInventory().contains(goldNecklaceUnnotedID)){
                if (craftedItemValue != 0){
                    return craftedItemValue;
                } else {
                    return 30;
                }
            } else {
                return 0;
            }
        }

        if (craftingGoldAmuletU){
            int craftedItemsInInventory = Math.toIntExact(getInventory().getAmount(goldAmuletUUnnotedID));
            boolean profitAdded = true;
            if (profitAdded && craftedItemsInInventory > totalItemsCrafted){
                totalItemsCrafted = totalItemsCrafted +1;
                profitAdded = false;
            }
            if (getInventory().contains(goldAmuletUUnnotedID) && !profitAdded|| totalValue == 0 && getInventory().contains(goldAmuletUUnnotedID)){
                if (craftedItemValue != 0){
                    return craftedItemValue;
                } else {
                    return 18;
                }
            } else {
                return 0;
            }
        }

        if (craftingSapphireNecklaces){
            int craftedItemsInInventory = Math.toIntExact(getInventory().getAmount(sapphireNecklaceUnnotedID));
            boolean profitAdded = true;
            if (profitAdded && craftedItemsInInventory > totalItemsCrafted){
                totalItemsCrafted = totalItemsCrafted +1;
                profitAdded = false;
            }
            if (getInventory().contains(sapphireNecklaceUnnotedID) && !profitAdded|| totalValue == 0 && getInventory().contains(sapphireNecklaceUnnotedID)){
                if (craftedItemValue != 0){
                    return craftedItemValue;
                } else {
                    return 60;
                }
            } else {
                return 0;
            }
        }

        if (craftingSapphireAmuletU){
            int craftedItemsInInventory = Math.toIntExact(getInventory().getAmount(sapphireAmuletUUnnotedID));
            boolean profitAdded = true;
            if (profitAdded && craftedItemsInInventory > totalItemsCrafted){
                totalItemsCrafted = totalItemsCrafted +1;
                profitAdded = false;
            }
            if (getInventory().contains(sapphireAmuletUUnnotedID) && !profitAdded|| totalValue == 0 && getInventory().contains(sapphireAmuletUUnnotedID)){
                if (craftedItemValue != 0){
                    return craftedItemValue;
                } else {
                    return -15;
                }
            } else {
                return 0;
            }
        }

        if (craftingEmeraldAmuletU){
            int craftedItemsInInventory = Math.toIntExact(getInventory().getAmount(emeraldAmuletUUnnotedID));
            boolean profitAdded = true;
            if (profitAdded && craftedItemsInInventory > totalItemsCrafted){
                totalItemsCrafted = totalItemsCrafted +1;
                profitAdded = false;
            }
            if (getInventory().contains(emeraldAmuletUUnnotedID) && !profitAdded|| totalValue == 0 && getInventory().contains(emeraldAmuletUUnnotedID)){
                if (craftedItemValue != 0){
                    return craftedItemValue;
                } else {
                    return 60;
                }
            } else {
                return 0;
            }
        }



        if (craftingRubyNecklaces){
            int craftedItemsInInventory = Math.toIntExact(getInventory().getAmount(rubyNecklaceUnnotedID));
            boolean profitAdded = true;
            if (profitAdded && craftedItemsInInventory > totalItemsCrafted){
                totalItemsCrafted = totalItemsCrafted +1;
                profitAdded = false;
            }
            if (getInventory().contains(rubyNecklaceUnnotedID) && !profitAdded|| totalValue == 0 && getInventory().contains(rubyNecklaceUnnotedID)){
                if (craftedItemValue != 0){
                    return craftedItemValue;
                } else {
                    return 100;
                }
            } else {
                return 0;
            }
        }

        if (craftingDiamondRings){
            int craftedItemsInInventory = Math.toIntExact(getInventory().getAmount(diamondRingUnnotedID));
            boolean profitAdded = true;
            if (profitAdded && craftedItemsInInventory > totalItemsCrafted){
                totalItemsCrafted = totalItemsCrafted +1;
                profitAdded = false;
            }
            if (getInventory().contains(diamondRingUnnotedID) && !profitAdded|| totalValue == 0 && getInventory().contains(diamondRingUnnotedID)){
                if (craftedItemValue != 0){
                    return craftedItemValue;
                } else {
                    return -70;
                }
            } else {
                return 0;
            }
        }

        if (craftingDiamondNecklaces){
            int craftedItemsInInventory = Math.toIntExact(getInventory().getAmount(diamondNecklaceUnnotedID));
            boolean profitAdded = true;
            if (profitAdded && craftedItemsInInventory > totalItemsCrafted){
                totalItemsCrafted = totalItemsCrafted +1;
                profitAdded = false;
            }
            if (getInventory().contains(diamondNecklaceUnnotedID) && !profitAdded|| totalValue == 0 && getInventory().contains(diamondNecklaceUnnotedID)){
                if (craftedItemValue != 0){
                    return craftedItemValue;
                } else {
                    return 30;
                }
            } else {
                return 0;
            }
        }

        if (craftingDiamondAmuletU){
            int craftedItemsInInventory = Math.toIntExact(getInventory().getAmount(diamondAmuletUUnnotedID));
            boolean profitAdded = true;
            if (profitAdded && craftedItemsInInventory > totalItemsCrafted){
                totalItemsCrafted = totalItemsCrafted +1;
                profitAdded = false;
            }
            if (getInventory().contains(diamondAmuletUUnnotedID) && !profitAdded|| totalValue == 0 && getInventory().contains(diamondAmuletUUnnotedID)){
                if (craftedItemValue != 0){
                    return craftedItemValue;
                } else {
                    return -70;
                }
            } else {
                return 0;
            }
        }
        return 0;
    }
}