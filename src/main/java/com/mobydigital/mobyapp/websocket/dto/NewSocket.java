package com.mobydigital.mobyapp.websocket.dto;

public class NewSocket   {
  private String type;     // "UPSERT" | "REMOVED"
  private String id;       // para REMOVED
  private Object payload; // para UPSERT
  private Boolean isMobyApp;
  private Boolean isMobyWeb;
}
