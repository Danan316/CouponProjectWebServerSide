package com.couponProject.main;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class TokenManager {

	private static final long EXPIRATION_TIME_PERIOD_IN_MILLIS = 1000*60*10; //10 min
	private static final long EXPIRATION_THREAD_PERIOD_IN_MILLIS = 1000*5; //5 seconds
	private static long lastToken = 6499;
	private Map<String, Token> tokens = new HashMap<String, TokenManager.Token>();

	
	public TokenManager() {
		super();
	}

	public Map<String, Token> getTokens() {
		return tokens;
	}

	synchronized public boolean isTokenExist(String token) {
		return tokens.containsKey(token);
	}

	synchronized public String getNewToken(String clientType) {
		Token newToken = new Token(clientType);
		tokens.put(newToken.getToken(), newToken);
		System.out.println("New token created by server: "+newToken.getToken());
		return newToken.getToken();
	}
	
	//synchronized
	public String getClientTypeByKey(String key) {
		return tokens.get(key).clientType;
	}
	
	/**
	 * initializing a timer thread that will operate each EXPIRATION_THREAD_PERIOD_IN_MILLIS millis
	 * and remove the expired tokens.
	 */
	public void initThread() {
	    int delay = 0; // delay for 0 sec.
	    Timer timer = new Timer();

		System.out.println("Remove Expired Sessions Thread Initialized!");
	    timer.scheduleAtFixedRate(new TimerTask() {
	      public void run() {
	        removeExpiredSessions();
	      }
	    }, delay, EXPIRATION_THREAD_PERIOD_IN_MILLIS);
	}
	
	synchronized private void removeExpiredSessions() {
		//System.out.println("Remove Expired Sessions Thread Started!");
		System.out.println("---------------------------------------------------------");
		Map<String, Token> remainedTokens = new HashMap<String, TokenManager.Token>();
		for (Token token : tokens.values()) {
			if (token.isExpired()) {
				System.out.println(token+" has expired!");
			}
			else {
				token.printRemainTimeInSeconds();
				remainedTokens.put(token.getToken(), token);
			}
		}
		tokens = remainedTokens;
		System.out.println("---------------------------------------------------------");
		System.out.println("Remove Expired Sessions Thread Done, active sessions:"+tokens.size());
	}
	
	public class Token {
		private String token;
		private String clientType;
		private long createTime;
		
		public Token(String clientType) {
			super();
			this.token = "Coupn_project_token_"+lastToken++;
			this.clientType = clientType;
			this.createTime = (new Date()).getTime();
		}

		public void printRemainTimeInSeconds() {
			long nowTime = (new Date()).getTime();
			long remainTime = (EXPIRATION_TIME_PERIOD_IN_MILLIS-(nowTime-createTime));
			String t="";
			if(remainTime<1000){
				t="Milliseconds";
			}
			else if(remainTime<60000) {
				t="Seconds";
				remainTime/=1000;
			}
			else {
				t="Minutes";
				remainTime/=(60*1000);
			}
			System.out.println(token+", Remaining time, a litle bit more than " +remainTime+" "+t);
		}

		public boolean isExpired()
		{
			long nowTime = (new Date()).getTime();
			return (nowTime-createTime)>EXPIRATION_TIME_PERIOD_IN_MILLIS;
		}
		public String getToken() {
			return token;
		}
		
		public String getClientType() {
			return clientType;
		}

		public long getCreateTime() {
			return createTime;
		}

		@Override
		public String toString() {
			return "Token [token=" + token + ", createTime=" + createTime + "]";
		}
	}

}
