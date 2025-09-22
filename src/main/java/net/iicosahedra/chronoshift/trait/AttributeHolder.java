package net.iicosahedra.chronoshift.trait;

import net.iicosahedra.chronoshift.util.ResourceLoc;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;


public class AttributeHolder {
    Holder<Attribute> attribute;
    String identifier;
    double value;
    AttributeModifier.Operation operation;

    public AttributeHolder(Holder<Attribute> attribute, String identifier, double value, AttributeModifier.Operation operation) {
        this.attribute = attribute;
        this.identifier = identifier;
        this.value = value;
        this.operation = operation;
    }

    public void applyModifier(LivingEntity entity, String id) {
        if(entity.getAttribute(attribute) != null){
            entity.getAttribute(attribute).addOrReplacePermanentModifier(new AttributeModifier(
                    ResourceLoc.create(identifier + "." + id), value, operation
            ));
        }
    }

    public void restoreDefault(LivingEntity entity, String id) {
        if(entity.getAttribute(attribute) != null){
            ResourceLocation rloc = ResourceLoc.create(identifier + "." + id);
            if(entity.getAttribute(attribute).hasModifier(rloc)) {
                entity.getAttribute(attribute).removeModifier(rloc);
            }
        }
    }
}