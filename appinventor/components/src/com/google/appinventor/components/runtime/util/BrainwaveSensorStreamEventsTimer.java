package com.google.appinventor.components.runtime.util;

import java.util.TimerTask;

import com.google.appinventor.components.runtime.BrainwaveSensor;

public class BrainwaveSensorStreamEventsTimer extends TimerTask {
	private BrainwaveSensor brainWaveSensorComponent;
	
	public BrainwaveSensorStreamEventsTimer(BrainwaveSensor brainWaveSensorComponent) {
		this.brainWaveSensorComponent = brainWaveSensorComponent;
	}
	
	@Override
	public void run() {
		brainWaveSensorComponent.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
            	//Channel AF3
            	if(brainWaveSensorComponent.channelAF3AlphaStream != null && brainWaveSensorComponent.channelAF3GammaStream != null &&
        				brainWaveSensorComponent.channelAF3HighBetaStream != null && brainWaveSensorComponent.channelAF3LowBetaStream != null &&
        						brainWaveSensorComponent.channelAF3ThetaStream != null) {
        			brainWaveSensorComponent.ChannelAF3ChangedStream(brainWaveSensorComponent.channelAF3ThetaStream, 
        					brainWaveSensorComponent.channelAF3AlphaStream, 
        					brainWaveSensorComponent.channelAF3LowBetaStream, 
        					brainWaveSensorComponent.channelAF3HighBetaStream, 
        					brainWaveSensorComponent.channelAF3GammaStream);
        			brainWaveSensorComponent.channelAF3ThetaStream = null;
        			brainWaveSensorComponent.channelAF3AlphaStream = null;
        			brainWaveSensorComponent.channelAF3LowBetaStream = null;
        			brainWaveSensorComponent.channelAF3HighBetaStream = null;
        			brainWaveSensorComponent.channelAF3GammaStream = null;
        		}
            	
            	//Channel F7
            	if(brainWaveSensorComponent.channelF7AlphaStream != null && brainWaveSensorComponent.channelF7GammaStream != null &&
        				brainWaveSensorComponent.channelF7HighBetaStream != null && brainWaveSensorComponent.channelF7LowBetaStream != null &&
        						brainWaveSensorComponent.channelF7ThetaStream != null) {
        			brainWaveSensorComponent.ChannelF7ChangedStream(brainWaveSensorComponent.channelF7ThetaStream, 
        					brainWaveSensorComponent.channelF7AlphaStream, 
        					brainWaveSensorComponent.channelF7LowBetaStream, 
        					brainWaveSensorComponent.channelF7HighBetaStream, 
        					brainWaveSensorComponent.channelF7GammaStream);
        			brainWaveSensorComponent.channelF7ThetaStream = null;
        			brainWaveSensorComponent.channelF7AlphaStream = null;
        			brainWaveSensorComponent.channelF7LowBetaStream = null;
        			brainWaveSensorComponent.channelF7HighBetaStream = null;
        			brainWaveSensorComponent.channelF7GammaStream = null;
        		}
            	
            	//Channel F3
            	if(brainWaveSensorComponent.channelF3AlphaStream != null && brainWaveSensorComponent.channelF3GammaStream != null &&
        				brainWaveSensorComponent.channelF3HighBetaStream != null && brainWaveSensorComponent.channelF3LowBetaStream != null &&
        						brainWaveSensorComponent.channelF3ThetaStream != null) {
        			brainWaveSensorComponent.ChannelF3ChangedStream(brainWaveSensorComponent.channelF3ThetaStream, 
        					brainWaveSensorComponent.channelF3AlphaStream, 
        					brainWaveSensorComponent.channelF3LowBetaStream, 
        					brainWaveSensorComponent.channelF3HighBetaStream, 
        					brainWaveSensorComponent.channelF3GammaStream);
        			brainWaveSensorComponent.channelF3ThetaStream = null;
        			brainWaveSensorComponent.channelF3AlphaStream = null;
        			brainWaveSensorComponent.channelF3LowBetaStream = null;
        			brainWaveSensorComponent.channelF3HighBetaStream = null;
        			brainWaveSensorComponent.channelF3GammaStream = null;
        		}
            	
            	//Channel FC5
            	if(brainWaveSensorComponent.channelFC5AlphaStream != null && brainWaveSensorComponent.channelFC5GammaStream != null &&
        				brainWaveSensorComponent.channelFC5HighBetaStream != null && brainWaveSensorComponent.channelFC5LowBetaStream != null &&
        						brainWaveSensorComponent.channelFC5ThetaStream != null) {
        			brainWaveSensorComponent.ChannelFC5ChangedStream(brainWaveSensorComponent.channelFC5ThetaStream, 
        					brainWaveSensorComponent.channelFC5AlphaStream, 
        					brainWaveSensorComponent.channelFC5LowBetaStream, 
        					brainWaveSensorComponent.channelFC5HighBetaStream, 
        					brainWaveSensorComponent.channelFC5GammaStream);
        			brainWaveSensorComponent.channelFC5ThetaStream = null;
        			brainWaveSensorComponent.channelFC5AlphaStream = null;
        			brainWaveSensorComponent.channelFC5LowBetaStream = null;
        			brainWaveSensorComponent.channelFC5HighBetaStream = null;
        			brainWaveSensorComponent.channelFC5GammaStream = null;
        		}
            	
            	//Channel T7
            	if(brainWaveSensorComponent.channelT7AlphaStream != null && brainWaveSensorComponent.channelT7GammaStream != null &&
        				brainWaveSensorComponent.channelT7HighBetaStream != null && brainWaveSensorComponent.channelT7LowBetaStream != null &&
        						brainWaveSensorComponent.channelT7ThetaStream != null) {
        			brainWaveSensorComponent.ChannelT7ChangedStream(brainWaveSensorComponent.channelT7ThetaStream, 
        					brainWaveSensorComponent.channelT7AlphaStream, 
        					brainWaveSensorComponent.channelT7LowBetaStream, 
        					brainWaveSensorComponent.channelT7HighBetaStream, 
        					brainWaveSensorComponent.channelT7GammaStream);
        			brainWaveSensorComponent.channelT7ThetaStream = null;
        			brainWaveSensorComponent.channelT7AlphaStream = null;
        			brainWaveSensorComponent.channelT7LowBetaStream = null;
        			brainWaveSensorComponent.channelT7HighBetaStream = null;
        			brainWaveSensorComponent.channelT7GammaStream = null;
        		}
            	
            	//Channel P7
            	if(brainWaveSensorComponent.channelP7AlphaStream != null && brainWaveSensorComponent.channelP7GammaStream != null &&
        				brainWaveSensorComponent.channelP7HighBetaStream != null && brainWaveSensorComponent.channelP7LowBetaStream != null &&
        						brainWaveSensorComponent.channelP7ThetaStream != null) {
        			brainWaveSensorComponent.ChannelP7ChangedStream(brainWaveSensorComponent.channelP7ThetaStream, 
        					brainWaveSensorComponent.channelP7AlphaStream, 
        					brainWaveSensorComponent.channelP7LowBetaStream, 
        					brainWaveSensorComponent.channelP7HighBetaStream, 
        					brainWaveSensorComponent.channelP7GammaStream);
        			brainWaveSensorComponent.channelP7ThetaStream = null;
        			brainWaveSensorComponent.channelP7AlphaStream = null;
        			brainWaveSensorComponent.channelP7LowBetaStream = null;
        			brainWaveSensorComponent.channelP7HighBetaStream = null;
        			brainWaveSensorComponent.channelP7GammaStream = null;
        		}
            	
            	//Channel Pz
            	if(brainWaveSensorComponent.channelPzAlphaStream != null && brainWaveSensorComponent.channelPzGammaStream != null &&
        				brainWaveSensorComponent.channelPzHighBetaStream != null && brainWaveSensorComponent.channelPzLowBetaStream != null &&
        						brainWaveSensorComponent.channelPzThetaStream != null) {
        			brainWaveSensorComponent.ChannelPzChangedStream(brainWaveSensorComponent.channelPzThetaStream, 
        					brainWaveSensorComponent.channelPzAlphaStream, 
        					brainWaveSensorComponent.channelPzLowBetaStream, 
        					brainWaveSensorComponent.channelPzHighBetaStream, 
        					brainWaveSensorComponent.channelPzGammaStream);
        			brainWaveSensorComponent.channelPzThetaStream = null;
        			brainWaveSensorComponent.channelPzAlphaStream = null;
        			brainWaveSensorComponent.channelPzLowBetaStream = null;
        			brainWaveSensorComponent.channelPzHighBetaStream = null;
        			brainWaveSensorComponent.channelPzGammaStream = null;
        		}
            	
            	//Channel O1
            	if(brainWaveSensorComponent.channelO1AlphaStream != null && brainWaveSensorComponent.channelO1GammaStream != null &&
        				brainWaveSensorComponent.channelO1HighBetaStream != null && brainWaveSensorComponent.channelO1LowBetaStream != null &&
        						brainWaveSensorComponent.channelO1ThetaStream != null) {
        			brainWaveSensorComponent.ChannelO1ChangedStream(brainWaveSensorComponent.channelO1ThetaStream, 
        					brainWaveSensorComponent.channelO1AlphaStream, 
        					brainWaveSensorComponent.channelO1LowBetaStream, 
        					brainWaveSensorComponent.channelO1HighBetaStream, 
        					brainWaveSensorComponent.channelO1GammaStream);
        			brainWaveSensorComponent.channelO1ThetaStream = null;
        			brainWaveSensorComponent.channelO1AlphaStream = null;
        			brainWaveSensorComponent.channelO1LowBetaStream = null;
        			brainWaveSensorComponent.channelO1HighBetaStream = null;
        			brainWaveSensorComponent.channelO1GammaStream = null;
        		}
            	
            	//Channel O2
            	if(brainWaveSensorComponent.channelO2AlphaStream != null && brainWaveSensorComponent.channelO2GammaStream != null &&
        				brainWaveSensorComponent.channelO2HighBetaStream != null && brainWaveSensorComponent.channelO2LowBetaStream != null &&
        						brainWaveSensorComponent.channelO2ThetaStream != null) {
        			brainWaveSensorComponent.ChannelO2ChangedStream(brainWaveSensorComponent.channelO2ThetaStream, 
        					brainWaveSensorComponent.channelO2AlphaStream, 
        					brainWaveSensorComponent.channelO2LowBetaStream, 
        					brainWaveSensorComponent.channelO2HighBetaStream, 
        					brainWaveSensorComponent.channelO2GammaStream);
        			brainWaveSensorComponent.channelO2ThetaStream = null;
        			brainWaveSensorComponent.channelO2AlphaStream = null;
        			brainWaveSensorComponent.channelO2LowBetaStream = null;
        			brainWaveSensorComponent.channelO2HighBetaStream = null;
        			brainWaveSensorComponent.channelO2GammaStream = null;
        		}
            	
            	//Channel P8
            	if(brainWaveSensorComponent.channelP8AlphaStream != null && brainWaveSensorComponent.channelP8GammaStream != null &&
        				brainWaveSensorComponent.channelP8HighBetaStream != null && brainWaveSensorComponent.channelP8LowBetaStream != null &&
        						brainWaveSensorComponent.channelP8ThetaStream != null) {
        			brainWaveSensorComponent.ChannelP8ChangedStream(brainWaveSensorComponent.channelP8ThetaStream, 
        					brainWaveSensorComponent.channelP8AlphaStream, 
        					brainWaveSensorComponent.channelP8LowBetaStream, 
        					brainWaveSensorComponent.channelP8HighBetaStream, 
        					brainWaveSensorComponent.channelP8GammaStream);
        			brainWaveSensorComponent.channelP8ThetaStream = null;
        			brainWaveSensorComponent.channelP8AlphaStream = null;
        			brainWaveSensorComponent.channelP8LowBetaStream = null;
        			brainWaveSensorComponent.channelP8HighBetaStream = null;
        			brainWaveSensorComponent.channelP8GammaStream = null;
        		}
            	
            	//Channel T8
            	if(brainWaveSensorComponent.channelT8AlphaStream != null && brainWaveSensorComponent.channelT8GammaStream != null &&
        				brainWaveSensorComponent.channelT8HighBetaStream != null && brainWaveSensorComponent.channelT8LowBetaStream != null &&
        						brainWaveSensorComponent.channelT8ThetaStream != null) {
        			brainWaveSensorComponent.ChannelT8ChangedStream(brainWaveSensorComponent.channelT8ThetaStream, 
        					brainWaveSensorComponent.channelT8AlphaStream, 
        					brainWaveSensorComponent.channelT8LowBetaStream, 
        					brainWaveSensorComponent.channelT8HighBetaStream, 
        					brainWaveSensorComponent.channelT8GammaStream);
        			brainWaveSensorComponent.channelT8ThetaStream = null;
        			brainWaveSensorComponent.channelT8AlphaStream = null;
        			brainWaveSensorComponent.channelT8LowBetaStream = null;
        			brainWaveSensorComponent.channelT8HighBetaStream = null;
        			brainWaveSensorComponent.channelT8GammaStream = null;
        		}
            	
            	//Channel FC6
            	if(brainWaveSensorComponent.channelFC6AlphaStream != null && brainWaveSensorComponent.channelFC6GammaStream != null &&
        				brainWaveSensorComponent.channelFC6HighBetaStream != null && brainWaveSensorComponent.channelFC6LowBetaStream != null &&
        						brainWaveSensorComponent.channelFC6ThetaStream != null) {
        			brainWaveSensorComponent.ChannelFC6ChangedStream(brainWaveSensorComponent.channelFC6ThetaStream, 
        					brainWaveSensorComponent.channelFC6AlphaStream, 
        					brainWaveSensorComponent.channelFC6LowBetaStream, 
        					brainWaveSensorComponent.channelFC6HighBetaStream, 
        					brainWaveSensorComponent.channelFC6GammaStream);
        			brainWaveSensorComponent.channelFC6ThetaStream = null;
        			brainWaveSensorComponent.channelFC6AlphaStream = null;
        			brainWaveSensorComponent.channelFC6LowBetaStream = null;
        			brainWaveSensorComponent.channelFC6HighBetaStream = null;
        			brainWaveSensorComponent.channelFC6GammaStream = null;
        		}
            	
            	//Channel F4
            	if(brainWaveSensorComponent.channelF4AlphaStream != null && brainWaveSensorComponent.channelF4GammaStream != null &&
        				brainWaveSensorComponent.channelF4HighBetaStream != null && brainWaveSensorComponent.channelF4LowBetaStream != null &&
        						brainWaveSensorComponent.channelF4ThetaStream != null) {
        			brainWaveSensorComponent.ChannelF4ChangedStream(brainWaveSensorComponent.channelF4ThetaStream, 
        					brainWaveSensorComponent.channelF4AlphaStream, 
        					brainWaveSensorComponent.channelF4LowBetaStream, 
        					brainWaveSensorComponent.channelF4HighBetaStream, 
        					brainWaveSensorComponent.channelF4GammaStream);
        			brainWaveSensorComponent.channelF4ThetaStream = null;
        			brainWaveSensorComponent.channelF4AlphaStream = null;
        			brainWaveSensorComponent.channelF4LowBetaStream = null;
        			brainWaveSensorComponent.channelF4HighBetaStream = null;
        			brainWaveSensorComponent.channelF4GammaStream = null;
        		}
            	
            	//Channel F8
            	if(brainWaveSensorComponent.channelF8AlphaStream != null && brainWaveSensorComponent.channelF8GammaStream != null &&
        				brainWaveSensorComponent.channelF8HighBetaStream != null && brainWaveSensorComponent.channelF8LowBetaStream != null &&
        						brainWaveSensorComponent.channelF8ThetaStream != null) {
        			brainWaveSensorComponent.ChannelF8ChangedStream(brainWaveSensorComponent.channelF8ThetaStream, 
        					brainWaveSensorComponent.channelF8AlphaStream, 
        					brainWaveSensorComponent.channelF8LowBetaStream, 
        					brainWaveSensorComponent.channelF8HighBetaStream, 
        					brainWaveSensorComponent.channelF8GammaStream);
        			brainWaveSensorComponent.channelF8ThetaStream = null;
        			brainWaveSensorComponent.channelF8AlphaStream = null;
        			brainWaveSensorComponent.channelF8LowBetaStream = null;
        			brainWaveSensorComponent.channelF8HighBetaStream = null;
        			brainWaveSensorComponent.channelF8GammaStream = null;
        		}
            	
            	//Channel AF4
            	if(brainWaveSensorComponent.channelAF4AlphaStream != null && brainWaveSensorComponent.channelAF4GammaStream != null &&
        				brainWaveSensorComponent.channelAF4HighBetaStream != null && brainWaveSensorComponent.channelAF4LowBetaStream != null &&
        						brainWaveSensorComponent.channelAF4ThetaStream != null) {
        			brainWaveSensorComponent.ChannelAF4ChangedStream(brainWaveSensorComponent.channelAF4ThetaStream, 
        					brainWaveSensorComponent.channelAF4AlphaStream, 
        					brainWaveSensorComponent.channelAF4LowBetaStream, 
        					brainWaveSensorComponent.channelAF4HighBetaStream, 
        					brainWaveSensorComponent.channelAF4GammaStream);
        			brainWaveSensorComponent.channelAF4ThetaStream = null;
        			brainWaveSensorComponent.channelAF4AlphaStream = null;
        			brainWaveSensorComponent.channelAF4LowBetaStream = null;
        			brainWaveSensorComponent.channelAF4HighBetaStream = null;
        			brainWaveSensorComponent.channelAF4GammaStream = null;
        		}
            }
        });
	}
}