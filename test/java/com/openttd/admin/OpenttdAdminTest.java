package com.openttd.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.openttd.TestUtils;
import com.openttd.admin.event.GameScriptEvent;
import com.openttd.admin.event.GameScriptEventListener;
import com.openttd.admin.event.PongEvent;
import com.openttd.admin.event.PongEventListener;
import com.openttd.admin.event.RConEndEvent;
import com.openttd.admin.event.RConEndEventListener;
import com.openttd.admin.model.JsonStringParameter;
import com.openttd.admin.model.RemoteGSJson;
import com.openttd.constant.CompanyID;
import com.openttd.constant.GSNews;
import com.openttd.gamescript.GSNewsPaper;
import com.openttd.network.core.Configuration;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test RCon and Ping/Pong
 *
 * @author erenard
 */
public class OpenttdAdminTest {

	private static final Logger log = LoggerFactory.getLogger(OpenttdAdminTest.class);
	private final Collection<PongEvent> pongEvents = new ArrayList<>();
	private final Collection<RConEndEvent> rConEndEvents = new ArrayList<>();
	private final Collection<GameScriptEvent> gameScriptEvents = new ArrayList<>();

	private final OpenttdTestAdmin openttdAdmin;

	public OpenttdAdminTest() throws IOException {
		Configuration configuration = new Configuration();
		configuration.host = TestUtils.readSecrets().getProperty("host");
		configuration.password = TestUtils.readSecrets().getProperty("admin_password");
		configuration.adminPort = Integer.parseInt(TestUtils.readSecrets().getProperty("admin_port"));
		openttdAdmin = new OpenttdTestAdmin(configuration);
		openttdAdmin.addListener(PongEvent.class, openttdAdmin);
		openttdAdmin.addListener(RConEndEvent.class, openttdAdmin);
		openttdAdmin.addListener(GameScriptEvent.class, openttdAdmin);
	}

	private class OpenttdTestAdmin extends OpenttdAdmin implements PongEventListener, RConEndEventListener, GameScriptEventListener {

		public OpenttdTestAdmin(Configuration configuration) {
			super(configuration);
		}

		@Override
		public void onGameScriptEvent(GameScriptEvent gameScriptEvent) {
			gameScriptEvents.add(gameScriptEvent);
		}

		@Override
		public void onPongEvent(PongEvent pongEvent) {
			pongEvents.add(pongEvent);
		}

		@Override
		public void onRConEndEvent(RConEndEvent rConEndEvent) {
			rConEndEvents.add(rConEndEvent);
		}
	}

	@BeforeEach
	public void setUp() {
		openttdAdmin.startup();
		pongEvents.clear();
		rConEndEvents.clear();
		gameScriptEvents.clear();
		TestUtils.wait(1);
	}

	@AfterEach
	public void tearDown() {
		openttdAdmin.shutdown();
	}

	@Test
	public void testPingPong() {
		var send = openttdAdmin.getSend();
		assertNotNull(send);
		for (long i = 0; i < 5; i++) {
			send.ping(i);
		}
		TestUtils.wait(1);
		assertEquals(5, pongEvents.size());
	}

	@Test
	public void testRCon() {
		var send = openttdAdmin.getSend();
		assertNotNull(send);
		for (long i = 0; i < 5; i++) {
			send.rcon("say message " + i);
			TestUtils.wait(1);
		}
		TestUtils.wait(1);
		assertEquals(5, rConEndEvents.size());
	}

	@Test
	public void testGameScript() {
		var gs = openttdAdmin.getGSExecutor();
		assertNotNull(gs);
		GSNewsPaper broadcast = new GSNewsPaper(GSNewsPaper.NewsType.NT_GENERAL, "News Broadcasting");
		gs.send(broadcast);
		TestUtils.wait(1);
		GSNewsPaper newsPaperCompany0 = new GSNewsPaper((short) 0, GSNewsPaper.NewsType.NT_GENERAL, "News for company 0");
		gs.send(newsPaperCompany0);
		TestUtils.wait(1);
		assertEquals(2, gameScriptEvents.size());
	}

	@Test
	public void testGameScriptNewspaper() {
		var networkAdminSender = openttdAdmin.getSend();
		assertNotNull(networkAdminSender);
		RemoteGSJson showNewsApiCall = new RemoteGSJson();
		showNewsApiCall.setMethod("GSNews.Create");
		ArrayList<Object> args = showNewsApiCall.getArgs();
		args.add(GSNews.NewsType.NT_GENERAL);
		args.add(new JsonStringParameter("Hello world!"));
		args.add(CompanyID.COMPANY_INVALID);
		args.add(GSNews.NewsReferenceType.NR_NONE.ordinal());
		args.add(-1);
		networkAdminSender.gameScript(showNewsApiCall.toJsonString());
	}

	@Test
	public void addMiscCosts() {
		var networkAdminSender = openttdAdmin.getSend();
		assertNotNull(networkAdminSender);
		RemoteGSJson changeBankBalanceCall = new RemoteGSJson();
		changeBankBalanceCall.setMethod("GSCompany.ChangeBankBalance");
		ArrayList<Object> args = changeBankBalanceCall.getArgs();
		args.add(0);
		args.add(1000);
		args.add("GSCompany.EXPENSES_OTHER");
		args.add("GSMap.TILE_INVALID");
		networkAdminSender.gameScript(changeBankBalanceCall.toJsonString());
	}

	@Test
	public void getIndustryId() {
		var networkAdminSender = openttdAdmin.getSend();
		assertNotNull(networkAdminSender);
		RemoteGSJson getIndustryIdCall = new RemoteGSJson();
		getIndustryIdCall.setMethod("GSIndustry.GetIndustryID");
		ArrayList<Object> args = getIndustryIdCall.getArgs();
		args.add(2762);
		networkAdminSender.gameScript(getIndustryIdCall.toJsonString());
	}

	@Test
	public void setIndustryProduction() {
		var networkAdminSender = openttdAdmin.getSend();
		assertNotNull(networkAdminSender);
		RemoteGSJson setIndustryProductionCall = new RemoteGSJson();
		setIndustryProductionCall.setMethod("GSIndustry.SetProductionLevel");
		ArrayList<Object> setIndustryProductionCallArgs = setIndustryProductionCall.getArgs();
		// IndustryID
		setIndustryProductionCallArgs.add(2);
		// ProdLevel 0 <= 128
		setIndustryProductionCallArgs.add(128);
		// ShowNews? 0 false, 1 true
		setIndustryProductionCallArgs.add(true);
		// News text
		setIndustryProductionCallArgs.add(new JsonStringParameter("Production has surged!"));
		networkAdminSender.gameScript(setIndustryProductionCall.toJsonString());
	}

	@Test
	public void industryTypeList() {
		var networkAdminSender = openttdAdmin.getSend();
		assertNotNull(networkAdminSender);
		RemoteGSJson getIndustryIdCall = new RemoteGSJson();
		getIndustryIdCall.setMethod("GSIndustryTypeList");
		networkAdminSender.gameScript(getIndustryIdCall.toJsonString());
	}

	@Test
	public void announceAwardingOfGrant() {
		// Issue grant
		int companyID = 0;
		long cashValue = 15000L;
		var networkAdminSender = openttdAdmin.getSend();
		assertNotNull(networkAdminSender);
		RemoteGSJson changeBankBalanceCall = new RemoteGSJson();
		changeBankBalanceCall.setMethod("GSCompany.ChangeBankBalance");
		ArrayList<Object> changeBankBalanceCallArgs = changeBankBalanceCall.getArgs();
		changeBankBalanceCallArgs.add(companyID);
		changeBankBalanceCallArgs.add(cashValue);
		changeBankBalanceCallArgs.add("GSCompany.EXPENSES_OTHER");
		changeBankBalanceCallArgs.add("GSMap.TILE_INVALID");
		networkAdminSender.gameScript(changeBankBalanceCall.toJsonString());

		// Show newspaper announcing grant to all companies
		String positive = " has been awarded a government bond grant of $";
		String negative = " is being sued for the amount of $";
		String action = null;
		if(cashValue > 0){
			action = positive;
		} else action = negative;

		RemoteGSJson showNewsApiCall = new RemoteGSJson();
		showNewsApiCall.setMethod("GSNews.Create");
		ArrayList<Object> newsApiCallArgs = showNewsApiCall.getArgs();
		newsApiCallArgs.add(GSNews.NewsType.NT_GENERAL);
		newsApiCallArgs.add(new JsonStringParameter("Company " + (companyID+1) + action + String.format("%,d", Math.abs(cashValue)*2) + "."));
		newsApiCallArgs.add(CompanyID.COMPANY_INVALID);
		// If below reference parameter is set to something other than NR_NONE, TileIndex ref_parameter must match news category
		newsApiCallArgs.add(GSNews.NewsReferenceType.NR_NONE.ordinal());
		// I.e. location referenced in newspaper must match type of newspaper reference we have passed
		newsApiCallArgs.add(-1);
		networkAdminSender.gameScript(showNewsApiCall.toJsonString());
	}

	@Test
	public void setMaxLoanAmt() {
		var networkAdminSender = openttdAdmin.getSend();
		assertNotNull(networkAdminSender);
		RemoteGSJson setLoanCall = new RemoteGSJson();
		setLoanCall.setMethod("GSCompany.SetMaxLoanAmountForCompany");
		ArrayList<Object> setLoanCallArgs = setLoanCall.getArgs();
		// Company to set max loan for
		setLoanCallArgs.add(0);
		// Value to set max loan to
		setLoanCallArgs.add(0);
		networkAdminSender.gameScript(setLoanCall.toJsonString());
	}

	@Test
	public void buildObject() {
		var networkAdminSender = openttdAdmin.getSend();
		assertNotNull(networkAdminSender);
		RemoteGSJson buildObjectCall = new RemoteGSJson();
		buildObjectCall.setMethod("GSObjectType.BuildObject");
		ArrayList<Object> buildObjectCallArgs = buildObjectCall.getArgs();
		// 3 for company-owned land
		// in OpenGfx landscape minus:
		// 14 for lighthouse, 12 for fenced-land
		buildObjectCallArgs.add(3);
		// view of object relative to player
		buildObjectCallArgs.add(0);
		// TileIndex location to build
		buildObjectCallArgs.add(2633L);
	}

	@Test
	public void purchaseCompanyLand() {
		var networkAdminSender = openttdAdmin.getSend();
		assertNotNull(networkAdminSender);
		RemoteGSJson buildObjectCall = new RemoteGSJson();
		buildObjectCall.setMethod("GSObjectType.BuildObject");
		ArrayList<Object> buildObjectCallArgs = buildObjectCall.getArgs();
		// 3 for company-owned land
		// in OpenGfx landscape minus:
		// 14 for lighthouse, 12 for fenced-land
		buildObjectCallArgs.add(3);
		// view of object relative to player
		buildObjectCallArgs.add(0);
		// TileIndex location to build
		buildObjectCallArgs.add(26981839L);
		// CompanyID of company to purchase land under
		buildObjectCall.setCompanyMode(0);
		networkAdminSender.gameScript(buildObjectCall.toJsonString());
	}

	@Test
	public void getObjectTypeViews() {
		var networkAdminSender = openttdAdmin.getSend();
		assertNotNull(networkAdminSender);
		RemoteGSJson getObjectTypeViewsCall = new RemoteGSJson();
		getObjectTypeViewsCall.setMethod("GSObjectType.GetViews");
		ArrayList<Object> buildObjectCallArgs = getObjectTypeViewsCall.getArgs();
		// 3 for company-owned land
		buildObjectCallArgs.add(3);
		networkAdminSender.gameScript(getObjectTypeViewsCall.toJsonString());
	}

	@Test
	public void getObjectTypeName() {
		var networkAdminSender = openttdAdmin.getSend();
		assertNotNull(networkAdminSender);
		RemoteGSJson getObjectTypeNameCall = new RemoteGSJson();
		getObjectTypeNameCall.setMethod("GSObjectType.GetName");
		ArrayList<Object> buildObjectCallArgs = getObjectTypeNameCall.getArgs();
		// 3 for company-owned land
		buildObjectCallArgs.add(3);
		networkAdminSender.gameScript(getObjectTypeNameCall.toJsonString());
	}

	@Test
	public void getIndustryType() {
		var networkAdminSender = openttdAdmin.getSend();
		assertNotNull(networkAdminSender);
		RemoteGSJson getIndustryTypeCall = new RemoteGSJson();
		getIndustryTypeCall.setMethod("GSIndustryType.ResolveNewGRFID");
		ArrayList<Object> getIndustryTypeCallArgs = getIndustryTypeCall.getArgs();
		// GRFID - AXIS
		getIndustryTypeCallArgs.add(0x4A4B8808);
		// GRF local ID - (0) - Coal Mine; (53) - Rubber plantation
		getIndustryTypeCallArgs.add(283);
		networkAdminSender.gameScript(getIndustryTypeCall.toJsonString());
		// Result: 1, 46
	}

	@Test
	public void buildIndustry() {
		var networkAdminSender = openttdAdmin.getSend();
		assertNotNull(networkAdminSender);
		RemoteGSJson buildIndustryCall = new RemoteGSJson();
		buildIndustryCall.setMethod("GSIndustryType.BuildIndustry");
		ArrayList<Object> buildIndustryCallArgs = buildIndustryCall.getArgs();
		// IndustryType - AXIS Coal Mine - see above
		buildIndustryCallArgs.add(0);
		// TileIndex location to build
		buildIndustryCallArgs.add(13641L);
		networkAdminSender.gameScript(buildIndustryCall.toJsonString());
	}

	@Test
	public void setIndustryText() {
		var networkAdminSender = openttdAdmin.getSend();
		assertNotNull(networkAdminSender);
		RemoteGSJson setIndustryTextCall = new RemoteGSJson();
		setIndustryTextCall.setMethod("GSIndustry.SetText");
		var args = setIndustryTextCall.getArgs();
		// Industry ID
		args.add(0);
		// Industry text
		args.add(new JsonStringParameter("Public Wharf No. 1"));
		networkAdminSender.gameScript(setIndustryTextCall.toJsonString());
	}

	@Test
	public void setExclusiveIndustryConsumerCall() {
		var networkAdminSender = openttdAdmin.getSend();
		assertNotNull(networkAdminSender);
		RemoteGSJson setExclusiveIndustryConsumerCall = new RemoteGSJson();
		setExclusiveIndustryConsumerCall.setMethod("GSIndustry.SetExclusiveConsumer");
		var args = setExclusiveIndustryConsumerCall.getArgs();
		// Industry ID
		args.add(0);
		// Company ID
		args.add(0);
		networkAdminSender.gameScript(setExclusiveIndustryConsumerCall.toJsonString());
	}

	@Test
	public void plantTreeRectangle() {
		var networkAdminSender = openttdAdmin.getSend();
		assertNotNull(networkAdminSender);
		RemoteGSJson plantTreeRectangleCall = new RemoteGSJson();
		plantTreeRectangleCall.setMethod("GSTile.PlantTree");
		ArrayList<Object> plantTreeCallArgs = plantTreeRectangleCall.getArgs();
		// TileIndex
		plantTreeCallArgs.add(3102L);
		networkAdminSender.gameScript(plantTreeRectangleCall.toJsonString());
	}

	@Test
	public void expandTown() {
		var networkAdminSender = openttdAdmin.getSend();
		assertNotNull(networkAdminSender);
		RemoteGSJson expandTownCall = new RemoteGSJson();
		expandTownCall.setMethod("GSTown.ExpandTown");
		ArrayList<Object> expandTownCallArgs = expandTownCall.getArgs();
		// TownID
		expandTownCallArgs.add(0);
		// Houses count
		expandTownCallArgs.add(40);
		networkAdminSender.gameScript(expandTownCall.toJsonString());
	}

	@Test
	public void setTownText() {
		var networkAdminSender = openttdAdmin.getSend();
		assertNotNull(networkAdminSender);
		RemoteGSJson setTownTextCall = new RemoteGSJson();
		setTownTextCall.setMethod("GSTown.SetText");
		ArrayList<Object> setTownTextCallArgs = setTownTextCall.getArgs();
		// TownID
		setTownTextCallArgs.add(0);
		// Text
		setTownTextCallArgs.add(new JsonStringParameter("Hello town window world!"));
		networkAdminSender.gameScript(setTownTextCall.toJsonString());
	}

	@Test
	public void setTownGrowthRate() {
		var networkAdminSender = openttdAdmin.getSend();
		assertNotNull(networkAdminSender);
		RemoteGSJson setTownGrowthRateCall = new RemoteGSJson();
		setTownGrowthRateCall.setMethod("GSTown.SetGrowthRate");
		int game_days_between_town_growth = 0;
		ArrayList<Object> args = setTownGrowthRateCall.getArgs();
		// TownID
		args.add(0);
		// Growth factor
		args.add(game_days_between_town_growth);
		networkAdminSender.gameScript(setTownGrowthRateCall.toJsonString());
	}

	@Test
	public void buildSign() {
		var networkAdminSender = openttdAdmin.getSend();
		assertNotNull(networkAdminSender);
		RemoteGSJson buildSignCall = new RemoteGSJson();
		buildSignCall.setMethod("GSSign.BuildSign");
		ArrayList<Object> buildSignCallArgs = buildSignCall.getArgs();
		// TileID
		buildSignCallArgs.add(10311L);
		// Growth factor
		buildSignCallArgs.add(new JsonStringParameter("Hello OpenTTD World!"));
		networkAdminSender.gameScript(buildSignCall.toJsonString());
	}
}
