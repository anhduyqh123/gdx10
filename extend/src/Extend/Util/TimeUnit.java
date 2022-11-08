package Extend.Util;

public enum TimeUnit {
    NANOSECONDS {
        public long toNanos(long var1) {
            return var1;
        }

        public long toMicros(long var1) {
            return var1 / 1000L;
        }

        public long toMillis(long var1) {
            return var1 / 1000000L;
        }

        public long toSeconds(long var1) {
            return var1 / 1000000000L;
        }

        public long toMinutes(long var1) {
            return var1 / 60000000000L;
        }

        public long toHours(long var1) {
            return var1 / 3600000000000L;
        }

        public long toDays(long var1) {
            return var1 / 86400000000000L;
        }

        public long convert(long var1, TimeUnit var3) {
            return var3.toNanos(var1);
        }
    },
    MICROSECONDS {
        public long toNanos(long var1) {
            return x(var1, 1000L, 9223372036854775L);
        }

        public long toMicros(long var1) {
            return var1;
        }

        public long toMillis(long var1) {
            return var1 / 1000L;
        }

        public long toSeconds(long var1) {
            return var1 / 1000000L;
        }

        public long toMinutes(long var1) {
            return var1 / 60000000L;
        }

        public long toHours(long var1) {
            return var1 / 3600000000L;
        }

        public long toDays(long var1) {
            return var1 / 86400000000L;
        }

        public long convert(long var1, TimeUnit var3) {
            return var3.toMicros(var1);
        }
    },
    MILLISECONDS {
        public long toNanos(long var1) {
            return x(var1, 1000000L, 9223372036854L);
        }

        public long toMicros(long var1) {
            return x(var1, 1000L, 9223372036854775L);
        }

        public long toMillis(long var1) {
            return var1;
        }

        public long toSeconds(long var1) {
            return var1 / 1000L;
        }

        public long toMinutes(long var1) {
            return var1 / 60000L;
        }

        public long toHours(long var1) {
            return var1 / 3600000L;
        }

        public long toDays(long var1) {
            return var1 / 86400000L;
        }

        public long convert(long var1, TimeUnit var3) {
            return var3.toMillis(var1);
        }
    },
    SECONDS {
        public long toNanos(long var1) {
            return x(var1, 1000000000L, 9223372036L);
        }

        public long toMicros(long var1) {
            return x(var1, 1000000L, 9223372036854L);
        }

        public long toMillis(long var1) {
            return x(var1, 1000L, 9223372036854775L);
        }

        public long toSeconds(long var1) {
            return var1;
        }

        public long toMinutes(long var1) {
            return var1 / 60L;
        }

        public long toHours(long var1) {
            return var1 / 3600L;
        }

        public long toDays(long var1) {
            return var1 / 86400L;
        }

        public long convert(long var1, TimeUnit var3) {
            return var3.toSeconds(var1);
        }
    },
    MINUTES {
        public long toNanos(long var1) {
            return x(var1, 60000000000L, 153722867L);
        }

        public long toMicros(long var1) {
            return x(var1, 60000000L, 153722867280L);
        }

        public long toMillis(long var1) {
            return x(var1, 60000L, 153722867280912L);
        }

        public long toSeconds(long var1) {
            return x(var1, 60L, 153722867280912930L);
        }

        public long toMinutes(long var1) {
            return var1;
        }

        public long toHours(long var1) {
            return var1 / 60L;
        }

        public long toDays(long var1) {
            return var1 / 1440L;
        }

        public long convert(long var1, TimeUnit var3) {
            return var3.toMinutes(var1);
        }
    },
    HOURS {
        public long toNanos(long var1) {
            return x(var1, 3600000000000L, 2562047L);
        }

        public long toMicros(long var1) {
            return x(var1, 3600000000L, 2562047788L);
        }

        public long toMillis(long var1) {
            return x(var1, 3600000L, 2562047788015L);
        }

        public long toSeconds(long var1) {
            return x(var1, 3600L, 2562047788015215L);
        }

        public long toMinutes(long var1) {
            return x(var1, 60L, 153722867280912930L);
        }

        public long toHours(long var1) {
            return var1;
        }

        public long toDays(long var1) {
            return var1 / 24L;
        }

        public long convert(long var1, TimeUnit var3) {
            return var3.toHours(var1);
        }
    },
    DAYS {
        public long toNanos(long var1) {
            return x(var1, 86400000000000L, 106751L);
        }

        public long toMicros(long var1) {
            return x(var1, 86400000000L, 106751991L);
        }

        public long toMillis(long var1) {
            return x(var1, 86400000L, 106751991167L);
        }

        public long toSeconds(long var1) {
            return x(var1, 86400L, 106751991167300L);
        }

        public long toMinutes(long var1) {
            return x(var1, 1440L, 6405119470038038L);
        }

        public long toHours(long var1) {
            return x(var1, 24L, 384307168202282325L);
        }

        public long toDays(long var1) {
            return var1;
        }

        public long convert(long var1, TimeUnit var3) {
            return var3.toDays(var1);
        }
    };

    static final long C0 = 1L;
    static final long C1 = 1000L;
    static final long C2 = 1000000L;
    static final long C3 = 1000000000L;
    static final long C4 = 60000000000L;
    static final long C5 = 3600000000000L;
    static final long C6 = 86400000000000L;
    static final long MAX = 9223372036854775807L;

    private TimeUnit() {
    }

    static long x(long var0, long var2, long var4) {
        if (var0 > var4) {
            return 9223372036854775807L;
        } else {
            return var0 < -var4 ? -9223372036854775808L : var0 * var2;
        }
    }

    public long convert(long var1, TimeUnit var3) {
        return 0;
    }

    public long toNanos(long var1) {
        return 0;
    }

    public long toMicros(long var1) {
        return 0;
    }

    public long toMillis(long var1) {
        return 0;
    }

    public long toSeconds(long var1) {
        return 0;
    }

    public long toMinutes(long var1) {
        return 0;
    }

    public long toHours(long var1) {
        return 0;
    }

    public long toDays(long var1) {
        return 0;
    }

    public static String SecondToHHMMSS(long second)
    {
        int hour = (int) SECONDS.toHours(second);
        second -=HOURS.toSeconds(hour);
        return Format(hour)+":"+SecondToMMSS(second);
    }
    public static String SecondToMMSS(long second)
    {
        int minute = (int)SECONDS.toMinutes(second);
        second -=MINUTES.toSeconds(minute);
        return Format(minute)+":"+Format((int)second);
    }
    private static String Format(int value)//00,01,02
    {
        if (value<10) return "0"+value;
        else return ""+value;
    }
}
