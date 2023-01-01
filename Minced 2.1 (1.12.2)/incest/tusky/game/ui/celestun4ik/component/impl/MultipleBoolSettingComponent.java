package incest.tusky.game.ui.celestun4ik.component.impl;

import incest.tusky.game.tuskevich;
import incest.tusky.game.ui.celestun4ik.panelcomponent;
import incest.tusky.game.ui.celestun4ik.component.Component;
import incest.tusky.game.ui.celestun4ik.component.ExpandableComponent;
import incest.tusky.game.ui.celestun4ik.component.PropertyComponent;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import incest.tusky.game.ui.settings.impl.BooleanSetting;
import incest.tusky.game.ui.settings.impl.MultipleBoolSetting;
import incest.tusky.game.module.impl.Render.ClickGUI;
import incest.tusky.game.ui.settings.Setting;
import incest.tusky.game.utils.render.RenderUtils;
import incest.tusky.game.utils.render.RoundedUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.util.ArrayList;

import static incest.tusky.game.module.impl.Render.ModuleList.oneColor;

public class MultipleBoolSettingComponent extends ExpandableComponent implements PropertyComponent {

	private final MultipleBoolSetting listSetting;
	Minecraft mc = Minecraft.getMinecraft();

	public MultipleBoolSettingComponent(Component parent, MultipleBoolSetting listSetting, int x, int y, int width,
										int height) {
		super(parent, listSetting.getName(), x, y, width, height);
		this.listSetting = listSetting;
	}

	@Override
	public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
		super.drawComponent(scaledResolution, mouseX, mouseY);
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		GlStateManager.pushMatrix();
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		RenderUtils.scissorRect(0, 25.5f, sr.getScaledWidth(), 239);
		int x = getX();
		int y = getY();
		int width = getWidth();
		int height = getHeight();
		int dropDownBoxY = y + 4;
		int textColor = 0xFFFFFF;
		RoundedUtil.drawRound(x + panelcomponent.X_ITEM_OFFSET - 2, y + height - 1 - 19, x - 3 + width - panelcomponent.X_ITEM_OFFSET-(x + panelcomponent.X_ITEM_OFFSET - 2), y + (isExpanded() ? getHeightWithExpand() + 18 : 39 )-(y + height - 1), 7, new Color(55, 55, 55, 200));

		mc.Nunito14.drawCenteredString(getName(), x - 2 + width / 2, dropDownBoxY - 1, new Color(220,220,220).getRGB());

		handleRender(x, y + getHeight() + 2, width, textColor);
		if (isExpanded()) {
		}
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
		GlStateManager.popMatrix();
	}

	@Override
	public void onMouseClick(int mouseX, int mouseY, int button) {
		super.onMouseClick(mouseX, mouseY, button);
		if (isExpanded()) {
			handleClick(mouseX, mouseY, getX(), getY() + getHeight() + 2, getWidth());
		}
	}

	private void handleRender(int x, int y, int width, int textColor) {
		int color = 0;
		ArrayList<BooleanSetting> a = new ArrayList();
		for (BooleanSetting e : listSetting.getBoolSettings()) {



			a.add(e);


			if (isExpanded()) {
				if (e.getCurrentValue()) {
					mc.Nunito14.drawCenteredString(e.getName(), x + panelcomponent.X_ITEM_OFFSET + width / 2 + 0.5f - 2, y + 2.5F, oneColor.getColorValue());
				} else {
					mc.Nunito14.drawCenteredString(e.getName(), x + panelcomponent.X_ITEM_OFFSET + width / 2 + 0.5f - 2, y + 2.5F, Color.WHITE.getRGB());
				}}
			y += (panelcomponent.ITEM_HEIGHT - 3);
		}
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		GlStateManager.pushMatrix();
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		RenderUtils.scissorRect(0, 25.5f, x + panelcomponent.X_ITEM_OFFSET + width + 5, 239);
		mc.Nunito14.drawString(a.get(0).getName() + (a.size()> 0 ? ", " + listSetting.getBoolSettings().get(1).getName()  : "Null"), x + panelcomponent.X_ITEM_OFFSET + width / 2 + 0.5f - 46, getY() + 2.5F + 10, -1);
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
		GlStateManager.popMatrix();


	}

	private void handleClick(int mouseX, int mouseY, int x, int y, int width) {
		for (BooleanSetting e : listSetting.getBoolSettings()) {
			if (mouseX >= x && mouseY >= y && mouseX <= x + width && mouseY <= y + panelcomponent.ITEM_HEIGHT - 3) {
				e.setBoolValue(!e.getCurrentValue());
			}

			y += panelcomponent.ITEM_HEIGHT - 3;
		}
	}

	@Override
	public int getHeightWithExpand() {
		return getHeight() + listSetting.getBoolSettings().toArray().length * (panelcomponent.ITEM_HEIGHT - 3);
	}

	@Override
	public void onPress(int mouseX, int mouseY, int button) {
	}

	@Override
	public boolean canExpand() {
		return listSetting.getBoolSettings().toArray().length > 0;
	}

	@Override
	public Setting getSetting() {
		return listSetting;
	}
}