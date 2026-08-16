package Barbatosishere.death_muffler_fix.mixin;

import mob_grinding_utils.MobGrindingUtils;
import net.neoforged.bus.api.IEventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * 阻止 MUG 的 doClientStuff() 注册缺失的 BossBarHidingEvent。
 *
 * MGU 1.1.10 构建缺陷：BossBarHidingEvent 只发布了 .java 源文件、没有编译
 * .class，客户端初始化执行 doClientStuff() 时会因加载缺失类而抛
 * NoClassDefFoundError。本 Mixin 仅跳过该次注册（doClientStuff 中第 4 次
 * IEventBus.register 调用），其余客户端功能（全局声音事件、XP 流体渲染层、
 * enqueueWork 任务等）全部保留；Boss 血条隐藏由 ClientHandler 重新实现。
 *
 * 注意：ordinal = 3 依赖 MUG 1.1.10 的调用顺序，MUG 更新后需重新核对。
 */
@Mixin(MobGrindingUtils.class)
public class DoClientStuffMixin {

    @Redirect(
            method = "doClientStuff",
            at = @At(value = "INVOKE", target = "net/neoforged/bus/api/IEventBus.register(Ljava/lang/Object;)V", ordinal = 3),
            remap = false
    )
    private void skipBossBarHidingEvent(IEventBus bus, Object listener) {
        // 空实现：跳过缺失类（BossBarHidingEvent）的注册
    }
}
