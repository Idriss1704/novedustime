package bf.novedus.novedustime.enums;

public enum CountryAndState {

	BadenWuertenberg("countrystate.de_bw"), Bayern("countrystate.de_by"), Berlin("countrystate.de_be"),
	Brandenburg("countrystate.de_bb"), Bremen("countrystate.de_hb"), Hamburg("countrystate.de_hh"),
	Hessen("countrystate.de_he"), MecklenburgVorpommern("countrystate.de_mv"), Niedersachsen("countrystate.de_ni"),
	NordrheinWestfalen("countrystate.de_nw"), RheinlandPfalz("countrystate.de_rp"), Saarland("countrystate.de_sl"),
	Sachsen("countrystate.de_sn"), SachsenAnhalt("countrystate.de_st"), SchleswigHolstein("countrystate.de_sh"),
	Thueringen("countrystate.de_th"), BurkinaFaso("countrystate.bfa"), Mali("countrystate.mali"),
	Niger("countrystate.niger"), IvoryCoast("countrystate.civ"), Ghana("countrystate.ghana"), Togo("countrystate.togo"),
	Benin("countrystate.benin");

	public final String label;

	private CountryAndState(String label) {
		this.label = label;
	}

}
