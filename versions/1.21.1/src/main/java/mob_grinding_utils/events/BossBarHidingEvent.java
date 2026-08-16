package mob_grinding_utils.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.CustomizeGuiOverlayEvent;

/**
 * Shim 补类：Mob Grinding Utils 1.1.10 的发布 jar 中该类只有 .java 源文件、
 * 未编译出 .class，其主类 doClientStuff() 执行到 new BossBarHidingEvent()
 * 即抛 NoClassDefFoundError，客户端初始化崩溃。
 *
 * 本模组将此类编译进自身 jar 补齐缺失字节码（同包同名），使 MGU 的
 * doClientStuff() 完整执行：血条隐藏、worldUnload、XP 流体渲染层、
 * 颜色处理器全部按 MGU 原逻辑生效，无需 Mixin 或逻辑补偿。
 *
 * 实现等价自 MGU 1.1.10 jar 内附源码（Mob Grinding Utils by vadis365）。
 * 若 MGU 后续版本补全了该类，两个 jar 将出现同名类，类路径任取其一，
 * 两者行为一致，不影响功能。
 */
@OnlyIn(Dist.CLIENT)
public class BossBarHidingEvent {

	@SubscribeEvent
	public void onRenderHUD(CustomizeGuiOverlayEvent.BossEventProgress event) {
		Player player = Minecraft.getInstance().player;
		if (player != null) {
			CompoundTag nbt = player.getPersistentData();
			if (nbt.getBoolean("MGU_WitherMuffle")) {
				String witherName = I18n.get("entity.minecraft.wither");
				if (event.getBossEvent().getName().getString().contains(witherName))
					event.setCanceled(true);
			}

			if (nbt.getBoolean("MGU_DragonMuffle")) {
				String dragonName = I18n.get("entity.minecraft.ender_dragon");
				if (event.getBossEvent().getName().getString().contains(dragonName))
					event.setCanceled(true);
			}
		}
	}
}
