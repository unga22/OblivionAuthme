package com.peti446.OblivionAuth.GUI;

import org.lwjgl.input.Keyboard;

import com.peti446.OblivionAuth.Herramientas;
import com.peti446.OblivionAuth.OblivionAuth;
import com.peti446.OblivionAuth.Packets.StringPacket;
import com.sun.org.apache.xml.internal.resolver.readers.OASISXMLCatalogReader;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;

@SideOnly(Side.CLIENT)
public class GuiLogin extends GuiScreen{
	private GuiTextField CampoDeRegisto;
	private GuiTextField CampoDeLogin;
	private GuiCheckBox recordar;
	private final String LoginORegistrarse;
	
	public GuiLogin(String loginorregister){
		LoginORegistrarse = loginorregister;
	}
	@Override
	public void updateScreen(){
		if(LoginORegistrarse.equals("Login")){
			CampoDeLogin.updateCursorCounter();
		} else {
			CampoDeRegisto.updateCursorCounter();
		}
	}
	@Override
	public void initGui(){
		Keyboard.enableRepeatEvents(true);
		buttonList.clear();
		buttonList.add(new GuiButton(0, width/2 - 100, height/4 + 96 + 12, LoginORegistrarse));
		buttonList.add(new GuiButton(1, width/2 - 100, height/4 + 120 + 12, I18n.format("gui.cancel")));
		buttonList.add(recordar = new GuiCheckBox(2, width/2 + 84, 85));
		String passGuardada = "";
		if(OblivionAuth.recordarPassFile.exists()){
			passGuardada = Herramientas.leerArchivo(OblivionAuth.recordarPassFile);
		}
		if(LoginORegistrarse.equals("Login")){
			CampoDeLogin = new GuiTextField(fontRendererObj, width / 2 - 100, 60, 200, 20);
			CampoDeLogin.setFocused(true);
			CampoDeLogin.setText(passGuardada != null ? passGuardada : "");
			recordar.setChecked(passGuardada != null && !passGuardada.isEmpty());
		} else {
			CampoDeRegisto = new GuiTextField(fontRendererObj, width / 2 - 100, 60, 200, 20);
			CampoDeRegisto.setFocused(true);
		}
	}
	@Override
	public void onGuiClosed(){
		Keyboard.enableRepeatEvents(false);
	}
	@Override
	protected void actionPerformed(GuiButton Boton) {
		if(Boton.id == 0){
			if(recordar.isChecked()){
				if(LoginORegistrarse.equals("Login")){
					Herramientas.GuardarArchivo(OblivionAuth.recordarPassFile, CampoDeLogin.getText());
				} else {
					Herramientas.GuardarArchivo(OblivionAuth.recordarPassFile, CampoDeRegisto.getText());
				}
			} else {
				OblivionAuth.recordarPassFile.delete();
			}
			String pass = "";
			if(LoginORegistrarse.equals("Login")){
				pass = Herramientas.CodificarPass(CampoDeLogin.getText());
			} else {
				pass = Herramientas.CodificarPass(CampoDeRegisto.getText());
			}
			OblivionAuth.Channel.sendToServer(new StringPacket(pass));
			mc.currentScreen = null;
		} else if(Boton.id == 1){
			mc.theWorld.sendQuittingDisconnectingPacket();
		} else if(Boton.id == 2){
			GuiCheckBox box = (GuiCheckBox) Boton;
			box.setChecked(!box.isChecked());
		}
	}
	@Override
	protected void keyTyped(char c, int i){
		if(LoginORegistrarse.equals("Login")){
			CampoDeLogin.textboxKeyTyped(c, i);
			((GuiButton) buttonList.get(0)).enabled = (CampoDeLogin.getText().trim().length() > 0);
		} else {
			CampoDeRegisto.textboxKeyTyped(c, i);
			((GuiButton) buttonList.get(0)).enabled = (CampoDeRegisto.getText().trim().length() > 0);
		}
	}
	@Override
	protected void mouseClicked(int i1, int i2, int i3){
		super.mouseClicked(i1, i2, i3);
		if(LoginORegistrarse.equals("Login")){
			CampoDeLogin.mouseClicked(i1, i2, i3);
		} else {
			CampoDeRegisto.mouseClicked(i1, i2, i3);
		}
	}
	@Override
	public void drawScreen(int i1, int i2, float f1){
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, LoginORegistrarse, width / 2, 20, 16777215);
		drawString(fontRendererObj, "This server is asking you to " + LoginORegistrarse.toLowerCase(), width / 2 - 100, 47, 10526880);
		drawString(fontRendererObj, "Remember ?", width / 2 -100, 90, 10526880);
		if(LoginORegistrarse.equals("Login")){
			CampoDeLogin.drawTextBox();
			((GuiButton) buttonList.get(0)).enabled = (CampoDeLogin.getText().trim().length() > 0);
		} else {
			CampoDeRegisto.drawTextBox();
			((GuiButton) buttonList.get(0)).enabled = (CampoDeRegisto.getText().trim().length() > 0);
		}
		super.drawScreen(i1, i2, f1);
	}
}