package allfish.modid;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.*;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class AllFish implements ModInitializer {
	public static final String MOD_ID = "allfish";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static boolean enabled = true;
	double prevPitch = 0.0;
	public static MinecraftClient mc;
	public static Timer timer = new Timer();
	public static Random r = new Random();
	public static Hand activeHand;
	SoundInstanceListener listener = new SoundInstanceListener() {
		@Override
		public void onSoundPlayed(SoundInstance sound, WeightedSoundSet soundSet, float range) {
			double pitch = sound.getPitch();
			String id = sound.getId().toString();
			if (sound.toString().equalsIgnoreCase("SoundInstance[minecraft:entity.experience_orb.pickup]")) {
				double x = sound.getX();
				double y = sound.getY();
				double z = sound.getZ();
				double bobberX = mc.player.fishHook.getX();
				double bobberY = mc.player.fishHook.getY();
				double bobberZ = mc.player.fishHook.getZ();

				if (Math.abs(bobberX-x) < 1 && Math.abs(bobberY-y) < 2 && Math.abs(bobberZ-z) < 1) {
					if (pitch != prevPitch) {
						prevPitch = pitch;
						//LOGGER.info(mc.player.getX() + " " + mc.player.getY() + " " + mc.player.getZ());
						LOGGER.info("Sound: " + sound + "pitch: " + pitch + " x: " + x + " y: " + y + " z: " + z);
						if (pitch > 1.20 && pitch < 1.75) {

									mc.interactionManager.interactItem(mc.player, activeHand);
									LOGGER.info("fished on red");

						} else if (pitch <= 1.3) {
							int randInt = r.nextInt(10);
							if (randInt == 1) {

										mc.interactionManager.interactItem(mc.player, activeHand);
										LOGGER.info("fished on NON-red");

							}
						}
					}
				}
			}


		}
	};
	@Override
	public void onInitialize() {
		LOGGER.info("AllFish Initialized");
		RegisterKeyBindings.register();
		ClientTickEvents.END_CLIENT_TICK.register(this::clientTickEvent);
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player == null) return;
			while (RegisterKeyBindings.toggleKey.wasPressed()) {
				enabled = !enabled;
				if (enabled) {
					LOGGER.info("AllFish Enabled");
					client.player.sendMessage(Text.literal("AllFish Enabled"), true);
				} else {
					LOGGER.info("AllFish Disabled");
					client.player.sendMessage(Text.literal("AllFish Disabled"), true);
				}
			}
		});

	}

	private void clientTickEvent(MinecraftClient mc) {
		this.mc = mc;
		if (mc.player == null || mc.world == null) {
			return;
		}
		if (mc.player.getActiveHand() != null) {
			activeHand = mc.player.getActiveHand();
		}
		SoundManager sm = mc.getSoundManager();
		if (enabled) {
			if (mc.player.getMainHandStack().getItem().asItem() == Items.FISHING_ROD) {
				sm.registerListener(listener);
			} else {
				sm.unregisterListener(listener);
			}

		}
	}
}