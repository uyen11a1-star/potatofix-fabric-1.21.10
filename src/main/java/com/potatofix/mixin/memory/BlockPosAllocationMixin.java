package com.potatofix.mixin.memory;

import com.potatofix.PotatoFixMod;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Tối ưu hóa bộ nhớ: Giảm thiểu việc new BlockPos() hàng vạn lần mỗi giây trong collision loops.
 * Cứu máy RAM 2GB-4GB khỏi tình trạng giật lag mỗi khi Java Garbage Collector (GC) dọn RAM.
 */
@Mixin(BlockPos.class)
public abstract class BlockPosAllocationMixin {
    private static final ThreadLocal<BlockPos.Mutable> REUSABLE_POS =
            ThreadLocal.withInitial(BlockPos.Mutable::new);

    public static BlockPos.Mutable getPooledMutable(int x, int y, int z) {
        if (!PotatoFixMod.CONFIG.gcAggressiveEviction) {
            return new BlockPos.Mutable(x, y, z);
        }
        BlockPos.Mutable pos = REUSABLE_POS.get();
        pos.set(x, y, z);
        return pos;
    }
}