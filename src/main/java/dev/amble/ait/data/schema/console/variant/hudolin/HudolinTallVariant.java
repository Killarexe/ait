
package dev.amble.ait.data.schema.console.variant.hudolin;


import net.minecraft.util.Identifier;

import dev.amble.ait.AITMod;
import dev.amble.ait.data.Loyalty;
import dev.amble.ait.data.schema.console.ConsoleVariantSchema;
import dev.amble.ait.data.schema.console.type.HudolinType;

public class HudolinTallVariant extends ConsoleVariantSchema {
    public static final Identifier REFERENCE = AITMod.id("console/hudolin_tall");

    public HudolinTallVariant() {
        super(HudolinType.REFERENCE, REFERENCE, new Loyalty(Loyalty.Type.OWNER));
    }
}
