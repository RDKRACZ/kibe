package io.github.lucaargolo.kibe.items.miscellaneous

import io.github.lucaargolo.kibe.mixin.CraftingTableContainerAccessor
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.screen.CraftingScreenHandler
import net.minecraft.screen.ScreenHandlerFactory
import net.minecraft.screen.SimpleNamedScreenHandlerFactory
import net.minecraft.text.LiteralText
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

@Suppress("CAST_NEVER_SUCCEEDS")
class PocketCraftingTable(settings: Settings): Item(settings) {

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        if(!world.isClient) {
            user.openHandledScreen(SimpleNamedScreenHandlerFactory(ScreenHandlerFactory { i, playerInventory, _ ->
                object: CraftingScreenHandler(i, playerInventory)  {
                    override fun onContentChanged(inventory: Inventory?) {
                        updateResult(syncId, world, (this as CraftingTableContainerAccessor).player, (this as CraftingTableContainerAccessor).input, (this as CraftingTableContainerAccessor).result)
                    }

                    override fun close(player: PlayerEntity?) {
                        super.close(player)
                        dropInventory(player, world, (this as CraftingTableContainerAccessor).input)
                    }
                }
            }, LiteralText("Pocket Crafting Table")))
        }
        return TypedActionResult.success(user.getStackInHand(hand))
    }


}