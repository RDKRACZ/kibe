package io.github.lucaargolo.kibe.blocks.tank

import alexiil.mc.lib.attributes.AttributeList
import alexiil.mc.lib.attributes.AttributeProvider
import alexiil.mc.lib.attributes.fluid.FluidAttributes
import alexiil.mc.lib.attributes.fluid.FluidInvUtil
import alexiil.mc.lib.attributes.fluid.amount.FluidAmount
import alexiil.mc.lib.attributes.fluid.volume.FluidKeys
import io.github.lucaargolo.kibe.mixin.BucketItemAccessor
import io.github.lucaargolo.kibe.mixin.BucketItemMixin
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.*
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.Fluids
import net.minecraft.item.BucketItem
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.World

class Tank: BlockWithEntity(FabricBlockSettings.of(Material.GLASS).strength(0.5F).nonOpaque().lightLevel { state -> state[Properties.LEVEL_15] }.sounds(BlockSoundGroup.GLASS)), AttributeProvider {

    init {
        defaultState = stateManager.defaultState.with(Properties.LEVEL_15, 0)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(Properties.LEVEL_15)
        super.appendProperties(builder)
    }

    override fun createBlockEntity(world: BlockView?) = TankBlockEntity(this)

    override fun getRenderType(state: BlockState?) = BlockRenderType.MODEL

    override fun addAllAttributes(world: World, pos: BlockPos?, state: BlockState?, to: AttributeList<*>) {
        (world.getBlockEntity(pos) as? TankBlockEntity)?.let {
            to.offer(it)
        }
    }

    override fun onUse(state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hit: BlockHitResult): ActionResult {
        return (world.getBlockEntity(pos) as? TankBlockEntity)?.let { FluidInvUtil.interactHandWithTank(it, player, hand).asActionResult() } ?: ActionResult.FAIL
    }

}