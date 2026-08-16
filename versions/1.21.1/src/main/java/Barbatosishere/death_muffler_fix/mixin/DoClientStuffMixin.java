package Barbatosishere.death_muffler_fix.mixin;

import mob_grinding_utils.MobGrindingUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 在 MUG 的 doClientStuff() 执行到缺失类的实例化前终止该方法。
 *
 * MGU 1.1.10 构建缺陷：BossBarHidingEvent 只发布了 .java 源文件、没有编译
 * .class，原方法执行到 new BossBarHidingEvent() 时即触发类加载并抛
 * NoClassDefFoundError，客户端初始化直接崩溃。本 Mixin 在 new 指令前
 * 取消执行（此时前面的 3 个事件注册已生效），Boss 血条隐藏由
 * ClientHandler 重新实现，XP 流体渲染层、颜色处理器注册与 worldUnload
 * 补偿由主类完成（见 Death_muffler_fix）。
 *
 * 注意：MUG 更新后若该方法修复，注入点不存在将导致启动失败（fail-fast）。
 */
@Mixin(MobGrindingUtils.class)
public class DoClientStuffMixin {

    @Inject(
            method = "doClientStuff",
            at = @At(value = "NEW", target = "mob_grinding_utils/events/BossBarHidingEvent"),
            cancellable = true,
            remap = false
    )
    private void skipMissingBossBarHidingEvent(CallbackInfo ci) {
        ci.cancel();
    }
}
