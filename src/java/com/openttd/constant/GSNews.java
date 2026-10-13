package com.openttd.constant;

@SuppressWarnings("unused")
public class GSNews {
	public static class NewsType {
		public static int NT_ACCIDENT = 2;
		public static int NT_COMPANY_INFO = 4;
		public static int NT_ECONOMY = 7;
		public static int NT_ADVICE = 11;
		public static int NT_ACCEPTANCE = 13;
		public static int NT_SUBSIDIES = 15;
		public static int NT_GENERAL = 16;
	}

	public enum NewsReferenceType {
		NR_NONE,
		NR_TILE,
		NR_STATION,
		NR_INDUSTRY,
		NR_TOWN,
	};
};
