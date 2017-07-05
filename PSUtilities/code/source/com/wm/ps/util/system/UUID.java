package com.wm.ps.util.system;

import java.security.SecureRandom;

public class UUID
{
  private static long node_id;
  private static short clock_seq;
  private static final long timeOffset = 0x1b21dd213814000L;
  private static final char hexChar[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

  static
  {
    node_id = 0L;
    clock_seq = 0;
    SecureRandom rand = new SecureRandom();
    byte b[] = new byte[6];
    rand.nextBytes(b);
    for (int i = 0; i < b.length; i++)
    {
      node_id |= ((long) b[i] & 255L) << i * 8;
    }

    node_id |= 0x800000000000L;
    b = new byte[2];
    rand.nextBytes(b);
    for (int i = 0; i < b.length; i++)
    {
      clock_seq = (short) (int) ((long) clock_seq | ((long) b[i] & 255L) << i * 8);
    }

    clock_seq &= 0x3fff;
    clock_seq |= 0x8000;
  }

  public UUID()
  {
  }

  public static String generate()
  {
    StringBuffer sb = new StringBuffer();
    long time = System.currentTimeMillis() * 10000L + timeOffset;
    int time_low = (int) (time & -1L);
    short time_mid = (short) (int) (time >>> 32 & 65535L);
    short time_hi_and_version = (short) (int) (time >> 48 & 4095L | 4096L);
    short seq = nextSeq();
    append(sb, time_low);
    sb.append("-");
    append(sb, time_mid);
    sb.append("-");
    append(sb, time_hi_and_version);
    sb.append("-");
    append(sb, seq);
    sb.append("-");
    append(sb, node_id);
    return sb.toString();
  }

  private static synchronized short nextSeq()
  {
    clock_seq = (short) (clock_seq + 1 & 0x3fff | 0x8000);
    return clock_seq;
  }

  private static void append(StringBuffer sb, int n)
  {
    byte b = (byte) (n >> 24);
    sb.append(hexChar[b >> 4 & 0xf]);
    sb.append(hexChar[b & 0xf]);
    b = (byte) (n >> 16);
    sb.append(hexChar[b >> 4 & 0xf]);
    sb.append(hexChar[b & 0xf]);
    b = (byte) (n >> 8);
    sb.append(hexChar[b >> 4 & 0xf]);
    sb.append(hexChar[b & 0xf]);
    b = (byte) n;
    sb.append(hexChar[b >> 4 & 0xf]);
    sb.append(hexChar[b & 0xf]);
  }

  private static void append(StringBuffer sb, short n)
  {
    byte b = (byte) (n >> 8);
    sb.append(hexChar[b >> 4 & 0xf]);
    sb.append(hexChar[b & 0xf]);
    b = (byte) n;
    sb.append(hexChar[b >> 4 & 0xf]);
    sb.append(hexChar[b & 0xf]);
  }

  private static void append(StringBuffer sb, long n)
  {
    byte b = (byte) (int) (n >> 40);
    sb.append(hexChar[b >> 4 & 0xf]);
    sb.append(hexChar[b & 0xf]);
    b = (byte) (int) (n >> 32);
    sb.append(hexChar[b >> 4 & 0xf]);
    sb.append(hexChar[b & 0xf]);
    b = (byte) (int) (n >> 24);
    sb.append(hexChar[b >> 4 & 0xf]);
    sb.append(hexChar[b & 0xf]);
    b = (byte) (int) (n >> 16);
    sb.append(hexChar[b >> 4 & 0xf]);
    sb.append(hexChar[b & 0xf]);
    b = (byte) (int) (n >> 8);
    sb.append(hexChar[b >> 4 & 0xf]);
    sb.append(hexChar[b & 0xf]);
    b = (byte) (int) n;
    sb.append(hexChar[b >> 4 & 0xf]);
    sb.append(hexChar[b & 0xf]);
  }
}
