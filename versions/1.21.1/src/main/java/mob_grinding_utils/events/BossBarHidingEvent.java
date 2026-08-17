package mob_grinding_utils.events;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.Event;

import javax.annotation.Nonnull;

/**
 * BossBarHidingEvent - MGU 1.1.10 事件类，用于处理Boss血条隐藏
 * 
 * 这个类作为shim补全MGU 1.1.10中缺失的编译版本，
 * 通过提供等价的BossBarHidingEvent来防止客户端崩溃。
 */
public class BossBarHidingEvent extends Event {
    
    private final ResourceLocation bossId;
    private final String bossName;
    
    public BossBarHidingEvent(ResourceLocation bossId, String bossName) {
        this.bossId = bossId;
        this.bossName = bossName;
    }
    
    /**
     * 获取Boss的ID
     */
    @Nonnull
    public ResourceLocation getBossId() {
        return bossId;
    }
    
    /**
     * 获取Boss的名称
     */
    @Nonnull
    public String getBossName() {
        return bossName;
    }
}