package net.iicosahedra.chronoshift.trait;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;

import java.util.List;

public class Trait {
    private String name;
    private Component displayName;
    private List<AttributeHolder> attributes;
    private Item droppedItem;
    private int tier;

    public Trait(String name, Component displayName, List<AttributeHolder> attributes, Item droppedItem, int tier) {
        this.name = name;
        this.displayName = displayName;
        this.attributes = attributes;
        this.droppedItem = droppedItem;
        this.tier = tier;
    }

    String getName(){
        return name;
    }

    Component getDisplayName(){
        return displayName;
    }

    List<AttributeHolder> getAttributes(){
        return attributes;
    }

    Item getDroppedItem(){
        return droppedItem;
    }

    int getTier(){
        return tier;
    }
}
