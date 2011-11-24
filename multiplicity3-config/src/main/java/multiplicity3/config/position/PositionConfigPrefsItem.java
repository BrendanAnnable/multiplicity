/*
 * Copyright (c) 2009 University of Durham, England
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'SynergySpace' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package multiplicity3.config.position;

import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.jme3.math.Vector2f;

import multiplicity3.config.ConfigurationApplication;
import multiplicity3.config.PreferencesItem;

public class PositionConfigPrefsItem implements PreferencesItem {

	private static final Preferences prefs = ConfigurationApplication.getPreferences(PositionConfigPrefsItem.class);

	public static final String CURRENT_CONNECTIONS = "CURRENT_CONNECTIONS";
	public static final String PREFS_LOCATION_X = "DISPLAY_LOCATION_X";
	public static final String PREFS_LOCATION_Y = "DISPLAY_LOCATION_Y";
	public static final String PREFS_ANGLE = "DISPLAY_ANGLE";
	public static final String PREFS_POS_DEVELOPER_MODE = "POS_DEVELOPER_MODE";
	public static final String HORIZONTAL_PLACEMENT = "HORIZONTAL_PLACEMENT";
	public static final String GRID_LIMIT_X = "GRID_LIMIT_X";
	public static final String GRID_LIMIT_Y = "GRID_LIMIT_Y";
	public static final String GRID_DISTANCE_X = "GRID_DISTANCE_X";
	public static final String GRID_DISTANCE_Y = "GRID_DISTANCE_Y";
	public static final String TABLE_WIDTH = "TABLE_WIDTH";
	public static final String REFERENCE_DISTANCE = "REFERENCE_DISTANCE";

	@Override
	public JPanel getConfigurationPanel() {
		JPanel panel = new JPanel();
		panel.add(new JButton("Position panel"));
		return new PositionConfigPanel(this);
	}

	@Override
	public String getConfigurationPanelName() {
		return "Position";
	}

	public boolean getDeveloperMode() {
		return prefs.get(PREFS_POS_DEVELOPER_MODE, "false").equals("true");
	}

	public boolean getHorizontalPlacement() {
		return prefs.get(HORIZONTAL_PLACEMENT, "false").equals("true");
	}


	public int getXPos() {
		return prefs.getInt(PREFS_LOCATION_X, 0);
	}

	public int getYPos() {
		return prefs.getInt(PREFS_LOCATION_Y, 0);
	}

	public Float getAngle() {
		return prefs.getFloat(PREFS_ANGLE, 0);
	}

	public int getGridDistanceX() {
		return prefs.getInt(GRID_DISTANCE_X, 0);
	}

	public int getGridDistanceY() {
		return prefs.getInt(GRID_DISTANCE_Y, 0);
	}

	public int getGridLimitX() {
		return prefs.getInt(GRID_LIMIT_X, 0);
	}

	public int getGridLimitY() {
		return prefs.getInt(GRID_LIMIT_Y, 0);
	}

	public void setDeveloperMode(boolean selected) {
		prefs.put(PREFS_POS_DEVELOPER_MODE, "" + selected);

	}

	public void setHorizontalPlacement(boolean selected) {
		prefs.put(HORIZONTAL_PLACEMENT, "" + selected);

	}

	public boolean setXPos(String text) {
		try{
			int xPos = Integer.parseInt(text);
			prefs.putInt(PREFS_LOCATION_X, xPos);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	public boolean setYPos(String text) {
		try{
			int yPos = Integer.parseInt(text);
			prefs.putInt(PREFS_LOCATION_Y, yPos);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	public boolean setAngle(String text) {
		try{
			float angle = Float.parseFloat(text);
			prefs.putFloat(PREFS_ANGLE, angle);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	public boolean setXDistance(String text) {
		try{
			int xDis = Integer.parseInt(text);
			prefs.putInt(GRID_DISTANCE_X, xDis);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	public boolean setYDistance(String text) {
		try{
			int yDis = Integer.parseInt(text);
			prefs.putInt(GRID_DISTANCE_Y, yDis);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	public boolean setXLimit(String text) {
		try{
			int xLimit = Integer.parseInt(text);
			prefs.putInt(GRID_LIMIT_X, xLimit);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	public boolean setYLimit(String text) {
		try{
			int yLimit = Integer.parseInt(text);
			prefs.putInt(GRID_LIMIT_Y, yLimit);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	
	public Vector2f getLocation(){
		int location_x = getXPos();
		int location_y = getYPos();

		if (getDeveloperMode()){
			if (getHorizontalPlacement()){
				if(getGridLimitX() != 0){
					int xPos = (prefs.getInt(PositionConfigPrefsItem.CURRENT_CONNECTIONS, 0)-1) % getGridLimitX();
					location_x = xPos * getGridDistanceX();
					int yPos = (prefs.getInt(PositionConfigPrefsItem.CURRENT_CONNECTIONS, 0)-1) / getGridLimitX();
					location_y = yPos * getGridDistanceY();
				}else{
					location_x = (prefs.getInt(PositionConfigPrefsItem.CURRENT_CONNECTIONS, 0)-1) * getGridDistanceX();
					location_y = 0;
				}
			}else{
				if(getGridLimitY() != 0){
					int yPos = (prefs.getInt(PositionConfigPrefsItem.CURRENT_CONNECTIONS, 0)-1) % getGridLimitY();
					location_y = yPos * getGridDistanceY();
					int xPos = (prefs.getInt(PositionConfigPrefsItem.CURRENT_CONNECTIONS, 0)-1) / getGridLimitY();
					location_x = xPos * getGridDistanceX();
				}else{
					location_y = (prefs.getInt(PositionConfigPrefsItem.CURRENT_CONNECTIONS, 0)-1) * getGridDistanceY();
					location_x = 0;
				}
			}
		}
		return new Vector2f(location_x, location_y);
	}
	
	public float getOrientation(){
		float angle = getAngle();
		if (getDeveloperMode())angle = 0;		
		return angle;
	}

}
