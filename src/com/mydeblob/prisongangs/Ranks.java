package com.mydeblob.prisongangs;


public enum Ranks {
	MEMBER,
	TRUSTED,
	OFFICER,
	LEADER,
	OWNER;
	
	public String toText(){
		if(this == MEMBER){
			return "Member";
		}else if(this == TRUSTED){
			return "Trusted";
		}else if(this == OFFICER){
			return "Officer";
		}else if(this == LEADER){
			return "Leader";
		}else if(this == OWNER){
			return "Owner";
		}else{
			return null;
		}
	}
}
