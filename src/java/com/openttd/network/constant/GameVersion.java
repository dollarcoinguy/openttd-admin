package com.openttd.network.constant;

public interface GameVersion {

	int MINOR_VERSION = 73;
	int PATCH_VERSION = 4;
	/* See .version */
	String OPENTTD = "jgrpp-0." + MINOR_VERSION + "." + PATCH_VERSION;
	/* See rev.cpp.in */
	long NEWGRF = 536898916;
}
