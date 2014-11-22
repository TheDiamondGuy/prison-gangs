package com.cullan.prisongangs;

public enum Ranks
{
  MEMBER,  TRUSTED,  OFFICER,  LEADER,  OWNER;
  
  public String toText()
  {
    if (this == MEMBER) {
      return "Member";
    }
    if (this == TRUSTED) {
      return "Trusted";
    }
    if (this == OFFICER) {
      return "Officer";
    }
    if (this == LEADER) {
      return "Leader";
    }
    if (this == OWNER) {
      return "Owner";
    }
    return null;
  }
}
