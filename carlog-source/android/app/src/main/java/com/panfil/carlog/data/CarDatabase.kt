package com.panfil.carlog.data

import com.panfil.carlog.R

data class CarGeneration(
    val name: String,
    val yearFrom: Int,
    val yearTo: Int,
)

data class CarModel(
    val name: String,
    val generations: List<CarGeneration>,
)

data class CarBrand(
    val name: String,
    val logoRes: Int,
    val models: List<CarModel>,
)

object CarDatabase {

    val brands: List<CarBrand> = listOf(
        CarBrand("Audi", R.drawable.logo_audi, listOf(
            CarModel("A1", listOf(
                CarGeneration("8X", 2010, 2018),
                CarGeneration("GB", 2018, 2025),
            )),
            CarModel("A3", listOf(
                CarGeneration("8L", 1996, 2003),
                CarGeneration("8P", 2003, 2012),
                CarGeneration("8V", 2012, 2020),
                CarGeneration("8Y", 2020, 2025),
            )),
            CarModel("A4", listOf(
                CarGeneration("B5", 1994, 2001),
                CarGeneration("B6", 2000, 2006),
                CarGeneration("B7", 2004, 2009),
                CarGeneration("B8", 2007, 2015),
                CarGeneration("B9", 2015, 2025),
            )),
            CarModel("A5", listOf(
                CarGeneration("8T", 2007, 2016),
                CarGeneration("F5", 2016, 2025),
            )),
            CarModel("A6", listOf(
                CarGeneration("C4", 1994, 1997),
                CarGeneration("C5", 1997, 2004),
                CarGeneration("C6", 2004, 2011),
                CarGeneration("C7", 2011, 2018),
                CarGeneration("C8", 2018, 2025),
            )),
            CarModel("A7", listOf(
                CarGeneration("4G", 2010, 2018),
                CarGeneration("4K", 2018, 2025),
            )),
            CarModel("A8", listOf(
                CarGeneration("D2", 1994, 2002),
                CarGeneration("D3", 2002, 2010),
                CarGeneration("D4", 2009, 2017),
                CarGeneration("D5", 2017, 2025),
            )),
            CarModel("Q3", listOf(
                CarGeneration("8U", 2011, 2018),
                CarGeneration("F3", 2018, 2025),
            )),
            CarModel("Q5", listOf(
                CarGeneration("8R", 2008, 2017),
                CarGeneration("FY", 2016, 2025),
            )),
            CarModel("Q7", listOf(
                CarGeneration("4L", 2005, 2015),
                CarGeneration("4M", 2015, 2025),
            )),
            CarModel("Q8", listOf(
                CarGeneration("4M8", 2018, 2025),
            )),
            CarModel("TT", listOf(
                CarGeneration("8N", 1998, 2006),
                CarGeneration("8J", 2006, 2014),
                CarGeneration("8S", 2014, 2023),
            )),
        )),
        CarBrand("BMW", R.drawable.logo_bmw, listOf(
            CarModel("1 серия", listOf(
                CarGeneration("E87", 2004, 2011),
                CarGeneration("F20", 2011, 2019),
                CarGeneration("F40", 2019, 2025),
            )),
            CarModel("2 серия", listOf(
                CarGeneration("F22", 2013, 2021),
                CarGeneration("G42", 2021, 2025),
            )),
            CarModel("3 серия", listOf(
                CarGeneration("E36", 1990, 2000),
                CarGeneration("E46", 1998, 2006),
                CarGeneration("E90", 2004, 2013),
                CarGeneration("F30", 2011, 2019),
                CarGeneration("G20", 2018, 2025),
            )),
            CarModel("4 серия", listOf(
                CarGeneration("F32", 2013, 2020),
                CarGeneration("G22", 2020, 2025),
            )),
            CarModel("5 серия", listOf(
                CarGeneration("E34", 1987, 1996),
                CarGeneration("E39", 1995, 2004),
                CarGeneration("E60", 2003, 2010),
                CarGeneration("F10", 2009, 2017),
                CarGeneration("G30", 2016, 2025),
            )),
            CarModel("6 серия", listOf(
                CarGeneration("E63", 2003, 2010),
                CarGeneration("F06", 2011, 2018),
            )),
            CarModel("7 серия", listOf(
                CarGeneration("E38", 1994, 2001),
                CarGeneration("E65", 2001, 2008),
                CarGeneration("F01", 2008, 2015),
                CarGeneration("G11", 2015, 2022),
                CarGeneration("G70", 2022, 2025),
            )),
            CarModel("X1", listOf(
                CarGeneration("E84", 2009, 2015),
                CarGeneration("F48", 2015, 2022),
                CarGeneration("U11", 2022, 2025),
            )),
            CarModel("X3", listOf(
                CarGeneration("E83", 2003, 2010),
                CarGeneration("F25", 2010, 2017),
                CarGeneration("G01", 2017, 2025),
            )),
            CarModel("X4", listOf(
                CarGeneration("F26", 2014, 2018),
                CarGeneration("G02", 2018, 2025),
            )),
            CarModel("X5", listOf(
                CarGeneration("E53", 1999, 2006),
                CarGeneration("E70", 2006, 2013),
                CarGeneration("F15", 2013, 2018),
                CarGeneration("G05", 2018, 2025),
            )),
            CarModel("X6", listOf(
                CarGeneration("E71", 2008, 2014),
                CarGeneration("F16", 2014, 2019),
                CarGeneration("G06", 2019, 2025),
            )),
        )),
        CarBrand("Changan", R.drawable.logo_changan, listOf(
            CarModel("CS35 Plus", listOf(CarGeneration("I", 2018, 2025))),
            CarModel("CS55 Plus", listOf(CarGeneration("I", 2017, 2025))),
            CarModel("CS75 Plus", listOf(CarGeneration("I", 2019, 2025))),
            CarModel("Uni-K", listOf(CarGeneration("I", 2021, 2025))),
            CarModel("Uni-V", listOf(CarGeneration("I", 2022, 2025))),
        )),
        CarBrand("Chery", R.drawable.logo_chery, listOf(
            CarModel("Tiggo 4", listOf(CarGeneration("I", 2017, 2025))),
            CarModel("Tiggo 7 Pro", listOf(CarGeneration("I", 2020, 2025))),
            CarModel("Tiggo 8 Pro", listOf(CarGeneration("I", 2020, 2025))),
            CarModel("Arrizo 8", listOf(CarGeneration("I", 2022, 2025))),
        )),
        CarBrand("Chevrolet", R.drawable.logo_chevrolet, listOf(
            CarModel("Aveo", listOf(
                CarGeneration("T200", 2002, 2008),
                CarGeneration("T250", 2006, 2011),
                CarGeneration("T300", 2011, 2020),
            )),
            CarModel("Cruze", listOf(
                CarGeneration("I", 2008, 2015),
                CarGeneration("II", 2015, 2020),
            )),
            CarModel("Cobalt", listOf(CarGeneration("I", 2011, 2020))),
            CarModel("Lacetti", listOf(CarGeneration("I", 2002, 2013))),
            CarModel("Niva", listOf(CarGeneration("I", 2002, 2025))),
            CarModel("Captiva", listOf(
                CarGeneration("I (C100)", 2006, 2011),
                CarGeneration("I рест. (C140)", 2011, 2018),
            )),
            CarModel("Orlando", listOf(CarGeneration("I", 2010, 2018))),
            CarModel("Spark", listOf(
                CarGeneration("II (M200)", 2005, 2010),
                CarGeneration("III (M300)", 2009, 2016),
            )),
            CarModel("Tahoe", listOf(
                CarGeneration("III (GMT900)", 2006, 2014),
                CarGeneration("IV (K2UG)", 2014, 2020),
                CarGeneration("V", 2020, 2025),
            )),
            CarModel("Camaro", listOf(
                CarGeneration("V", 2009, 2015),
                CarGeneration("VI", 2015, 2024),
            )),
        )),
        CarBrand("Citroën", R.drawable.logo_citroen, listOf(
            CarModel("C3", listOf(
                CarGeneration("I", 2002, 2009),
                CarGeneration("II", 2009, 2016),
                CarGeneration("III", 2016, 2025),
            )),
            CarModel("C4", listOf(
                CarGeneration("I", 2004, 2010),
                CarGeneration("II", 2010, 2018),
                CarGeneration("III", 2020, 2025),
            )),
            CarModel("C5", listOf(
                CarGeneration("I", 2001, 2008),
                CarGeneration("II", 2007, 2017),
                CarGeneration("X", 2021, 2025),
            )),
            CarModel("C-Crosser", listOf(CarGeneration("I", 2007, 2013))),
            CarModel("C4 Aircross", listOf(CarGeneration("I", 2012, 2017))),
            CarModel("Berlingo", listOf(
                CarGeneration("II", 2008, 2018),
                CarGeneration("III", 2018, 2025),
            )),
        )),
        CarBrand("Daewoo", R.drawable.logo_daewoo, listOf(
            CarModel("Matiz", listOf(CarGeneration("I", 1998, 2015))),
            CarModel("Nexia", listOf(
                CarGeneration("I", 1994, 2008),
                CarGeneration("II", 2008, 2016),
            )),
            CarModel("Gentra", listOf(CarGeneration("I", 2005, 2011))),
            CarModel("Lanos", listOf(CarGeneration("I", 1997, 2009))),
        )),
        CarBrand("Fiat", R.drawable.logo_fiat, listOf(
            CarModel("500", listOf(
                CarGeneration("I", 2007, 2020),
                CarGeneration("500e", 2020, 2025),
            )),
            CarModel("Punto", listOf(
                CarGeneration("II", 1999, 2007),
                CarGeneration("III", 2005, 2018),
            )),
            CarModel("Ducato", listOf(
                CarGeneration("III", 2006, 2014),
                CarGeneration("III рест.", 2014, 2025),
            )),
            CarModel("Albea", listOf(CarGeneration("I", 2002, 2012))),
        )),
        CarBrand("Ford", R.drawable.logo_ford, listOf(
            CarModel("EcoSport", listOf(
                CarGeneration("I", 2003, 2012),
                CarGeneration("II", 2012, 2022),
            )),
            CarModel("Explorer", listOf(
                CarGeneration("IV", 2005, 2010),
                CarGeneration("V", 2010, 2019),
                CarGeneration("VI", 2019, 2025),
            )),
            CarModel("Fiesta", listOf(
                CarGeneration("V", 2001, 2008),
                CarGeneration("VI", 2008, 2017),
                CarGeneration("VII", 2017, 2023),
            )),
            CarModel("Focus", listOf(
                CarGeneration("I", 1998, 2005),
                CarGeneration("II", 2004, 2011),
                CarGeneration("III", 2010, 2018),
                CarGeneration("IV", 2018, 2025),
            )),
            CarModel("Fusion", listOf(CarGeneration("I", 2002, 2012))),
            CarModel("Galaxy", listOf(
                CarGeneration("II", 2006, 2015),
                CarGeneration("III", 2015, 2022),
            )),
            CarModel("Kuga", listOf(
                CarGeneration("I", 2008, 2012),
                CarGeneration("II", 2012, 2019),
                CarGeneration("III", 2019, 2025),
            )),
            CarModel("Mondeo", listOf(
                CarGeneration("III", 2000, 2007),
                CarGeneration("IV", 2007, 2014),
                CarGeneration("V", 2014, 2022),
            )),
            CarModel("Ranger", listOf(
                CarGeneration("III (T6)", 2011, 2022),
                CarGeneration("IV (P703)", 2022, 2025),
            )),
            CarModel("S-MAX", listOf(
                CarGeneration("I", 2006, 2015),
                CarGeneration("II", 2015, 2023),
            )),
            CarModel("Transit", listOf(
                CarGeneration("VI", 2006, 2013),
                CarGeneration("VII", 2013, 2025),
            )),
        )),
        CarBrand("Geely", R.drawable.logo_geely, listOf(
            CarModel("Atlas", listOf(CarGeneration("I", 2016, 2025))),
            CarModel("Atlas Pro", listOf(CarGeneration("I", 2020, 2025))),
            CarModel("Coolray", listOf(CarGeneration("I", 2019, 2025))),
            CarModel("Monjaro", listOf(CarGeneration("I", 2022, 2025))),
            CarModel("Emgrand", listOf(
                CarGeneration("EC7", 2009, 2018),
                CarGeneration("IV", 2021, 2025),
            )),
            CarModel("Tugella", listOf(CarGeneration("I", 2020, 2025))),
        )),
        CarBrand("Haval", R.drawable.logo_haval, listOf(
            CarModel("F7", listOf(CarGeneration("I", 2018, 2025))),
            CarModel("F7x", listOf(CarGeneration("I", 2019, 2025))),
            CarModel("H6", listOf(CarGeneration("III", 2020, 2025))),
            CarModel("H9", listOf(CarGeneration("I", 2014, 2025))),
            CarModel("Jolion", listOf(CarGeneration("I", 2020, 2025))),
            CarModel("Dargo", listOf(CarGeneration("I", 2021, 2025))),
        )),
        CarBrand("Honda", R.drawable.logo_honda, listOf(
            CarModel("Accord", listOf(
                CarGeneration("VII", 2002, 2008),
                CarGeneration("VIII", 2007, 2013),
                CarGeneration("IX", 2012, 2017),
                CarGeneration("X", 2017, 2025),
            )),
            CarModel("Civic", listOf(
                CarGeneration("VII", 2000, 2006),
                CarGeneration("VIII", 2005, 2012),
                CarGeneration("IX", 2011, 2016),
                CarGeneration("X", 2015, 2021),
                CarGeneration("XI", 2021, 2025),
            )),
            CarModel("CR-V", listOf(
                CarGeneration("I", 1995, 2001),
                CarGeneration("II", 2001, 2006),
                CarGeneration("III", 2006, 2012),
                CarGeneration("IV", 2011, 2018),
                CarGeneration("V", 2016, 2025),
            )),
            CarModel("Fit / Jazz", listOf(
                CarGeneration("I (GD)", 2001, 2008),
                CarGeneration("II (GE)", 2007, 2013),
                CarGeneration("III (GK)", 2013, 2020),
                CarGeneration("IV (GR)", 2020, 2025),
            )),
            CarModel("HR-V", listOf(
                CarGeneration("I (GH)", 1998, 2006),
                CarGeneration("II (RU)", 2013, 2021),
                CarGeneration("III", 2021, 2025),
            )),
            CarModel("Pilot", listOf(
                CarGeneration("I", 2002, 2008),
                CarGeneration("II", 2008, 2015),
                CarGeneration("III", 2015, 2025),
            )),
        )),
        CarBrand("Hyundai", R.drawable.logo_hyundai, listOf(
            CarModel("Accent", listOf(
                CarGeneration("II (LC)", 1999, 2005),
                CarGeneration("III (MC)", 2005, 2010),
                CarGeneration("IV (RB)", 2010, 2017),
                CarGeneration("V (HC)", 2017, 2025),
            )),
            CarModel("Creta", listOf(
                CarGeneration("I", 2015, 2021),
                CarGeneration("II", 2021, 2025),
            )),
            CarModel("Elantra", listOf(
                CarGeneration("IV (HD)", 2006, 2011),
                CarGeneration("V (MD)", 2010, 2016),
                CarGeneration("VI (AD)", 2015, 2020),
                CarGeneration("VII (CN7)", 2020, 2025),
            )),
            CarModel("Getz", listOf(CarGeneration("I", 2002, 2011))),
            CarModel("i30", listOf(
                CarGeneration("I (FD)", 2007, 2012),
                CarGeneration("II (GD)", 2011, 2017),
                CarGeneration("III (PD)", 2016, 2025),
            )),
            CarModel("ix35", listOf(CarGeneration("I (LM)", 2009, 2015))),
            CarModel("Palisade", listOf(CarGeneration("I (LX2)", 2018, 2025))),
            CarModel("Santa Fe", listOf(
                CarGeneration("I (SM)", 2000, 2006),
                CarGeneration("II (CM)", 2006, 2012),
                CarGeneration("III (DM)", 2012, 2018),
                CarGeneration("IV (TM)", 2018, 2025),
            )),
            CarModel("Solaris", listOf(
                CarGeneration("I", 2010, 2017),
                CarGeneration("II", 2017, 2025),
            )),
            CarModel("Sonata", listOf(
                CarGeneration("V (NF)", 2004, 2010),
                CarGeneration("VI (YF)", 2009, 2014),
                CarGeneration("VII (LF)", 2014, 2019),
                CarGeneration("VIII (DN8)", 2019, 2025),
            )),
            CarModel("Tucson", listOf(
                CarGeneration("I (JM)", 2004, 2010),
                CarGeneration("II (LM)", 2009, 2015),
                CarGeneration("III (TL)", 2015, 2020),
                CarGeneration("IV (NX4)", 2020, 2025),
            )),
        )),
        CarBrand("Infiniti", R.drawable.logo_infiniti, listOf(
            CarModel("FX / QX70", listOf(
                CarGeneration("FX35/45 (S50)", 2002, 2008),
                CarGeneration("FX35/50 (S51)", 2008, 2017),
            )),
            CarModel("G / Q50", listOf(
                CarGeneration("G35 (V35)", 2002, 2007),
                CarGeneration("G37 (V36)", 2006, 2013),
                CarGeneration("Q50 (V37)", 2013, 2025),
            )),
            CarModel("QX50", listOf(
                CarGeneration("I (J50)", 2007, 2017),
                CarGeneration("II (QX50)", 2018, 2025),
            )),
            CarModel("QX60", listOf(
                CarGeneration("I (L50)", 2012, 2020),
                CarGeneration("II (L51)", 2021, 2025),
            )),
            CarModel("QX80", listOf(
                CarGeneration("I (Z62)", 2010, 2025),
            )),
        )),
        CarBrand("Jeep", R.drawable.logo_jeep, listOf(
            CarModel("Cherokee", listOf(
                CarGeneration("XJ", 1984, 2001),
                CarGeneration("KL", 2013, 2023),
            )),
            CarModel("Grand Cherokee", listOf(
                CarGeneration("WJ", 1998, 2004),
                CarGeneration("WK", 2004, 2010),
                CarGeneration("WK2", 2010, 2021),
                CarGeneration("WL", 2021, 2025),
            )),
            CarModel("Compass", listOf(
                CarGeneration("I (MK49)", 2006, 2016),
                CarGeneration("II (MP)", 2016, 2025),
            )),
            CarModel("Wrangler", listOf(
                CarGeneration("TJ", 1996, 2006),
                CarGeneration("JK", 2006, 2018),
                CarGeneration("JL", 2017, 2025),
            )),
        )),
        CarBrand("Kia", R.drawable.logo_kia, listOf(
            CarModel("Carnival", listOf(
                CarGeneration("II (VQ)", 2006, 2014),
                CarGeneration("III (KA4)", 2014, 2020),
                CarGeneration("IV", 2020, 2025),
            )),
            CarModel("Ceed", listOf(
                CarGeneration("I (ED)", 2006, 2012),
                CarGeneration("II (JD)", 2012, 2018),
                CarGeneration("III (CD)", 2018, 2025),
            )),
            CarModel("Cerato", listOf(
                CarGeneration("I (LD)", 2003, 2008),
                CarGeneration("II (TD)", 2008, 2013),
                CarGeneration("III (YD)", 2013, 2018),
                CarGeneration("IV (BD)", 2018, 2025),
            )),
            CarModel("Mohave", listOf(
                CarGeneration("I (HM)", 2008, 2019),
                CarGeneration("II", 2019, 2025),
            )),
            CarModel("Optima / K5", listOf(
                CarGeneration("III (MG)", 2010, 2015),
                CarGeneration("IV (JF)", 2015, 2020),
                CarGeneration("V (DL3)", 2019, 2025),
            )),
            CarModel("Picanto", listOf(
                CarGeneration("I (SA)", 2004, 2011),
                CarGeneration("II (TA)", 2011, 2017),
                CarGeneration("III (JA)", 2017, 2025),
            )),
            CarModel("Rio", listOf(
                CarGeneration("I (DC)", 2000, 2005),
                CarGeneration("II (JB)", 2005, 2011),
                CarGeneration("III (UB)", 2011, 2017),
                CarGeneration("IV (FB)", 2017, 2025),
            )),
            CarModel("Seltos", listOf(CarGeneration("I", 2019, 2025))),
            CarModel("Sorento", listOf(
                CarGeneration("I (BL)", 2002, 2009),
                CarGeneration("II (XM)", 2009, 2014),
                CarGeneration("III (UM)", 2014, 2020),
                CarGeneration("IV (MQ4)", 2020, 2025),
            )),
            CarModel("Soul", listOf(
                CarGeneration("I (AM)", 2008, 2013),
                CarGeneration("II (PS)", 2013, 2019),
                CarGeneration("III (SK3)", 2019, 2025),
            )),
            CarModel("Sportage", listOf(
                CarGeneration("I", 1993, 2004),
                CarGeneration("II (KM)", 2004, 2010),
                CarGeneration("III (SL)", 2010, 2015),
                CarGeneration("IV (QL)", 2015, 2021),
                CarGeneration("V (NQ5)", 2021, 2025),
            )),
            CarModel("Stinger", listOf(CarGeneration("I (CK)", 2017, 2023))),
        )),
        CarBrand("Lada (ВАЗ)", R.drawable.logo_lada, listOf(
            CarModel("2107", listOf(CarGeneration("I", 1982, 2012))),
            CarModel("2110", listOf(CarGeneration("I", 1996, 2007))),
            CarModel("2114", listOf(CarGeneration("I", 2001, 2013))),
            CarModel("Granta", listOf(
                CarGeneration("I", 2011, 2018),
                CarGeneration("I рестайлинг", 2018, 2025),
            )),
            CarModel("Kalina", listOf(
                CarGeneration("I", 2004, 2013),
                CarGeneration("II", 2013, 2018),
            )),
            CarModel("Largus", listOf(
                CarGeneration("I", 2012, 2021),
                CarGeneration("I рестайлинг", 2021, 2025),
            )),
            CarModel("Niva (4x4)", listOf(CarGeneration("2121", 1977, 2025))),
            CarModel("Niva Travel", listOf(CarGeneration("I", 2020, 2025))),
            CarModel("Priora", listOf(CarGeneration("I", 2007, 2018))),
            CarModel("Vesta", listOf(
                CarGeneration("I", 2015, 2022),
                CarGeneration("II (NG)", 2022, 2025),
            )),
            CarModel("XRAY", listOf(CarGeneration("I", 2015, 2022))),
        )),
        CarBrand("Land Rover", R.drawable.logo_landrover, listOf(
            CarModel("Defender", listOf(
                CarGeneration("I", 1983, 2016),
                CarGeneration("II (L663)", 2019, 2025),
            )),
            CarModel("Discovery", listOf(
                CarGeneration("III (L319)", 2004, 2009),
                CarGeneration("IV (L319)", 2009, 2016),
                CarGeneration("V (L462)", 2016, 2025),
            )),
            CarModel("Discovery Sport", listOf(CarGeneration("I (L550)", 2014, 2025))),
            CarModel("Freelander", listOf(
                CarGeneration("I", 1997, 2006),
                CarGeneration("II (L359)", 2006, 2014),
            )),
            CarModel("Range Rover", listOf(
                CarGeneration("III (L322)", 2002, 2012),
                CarGeneration("IV (L405)", 2012, 2021),
                CarGeneration("V (L460)", 2021, 2025),
            )),
            CarModel("Range Rover Evoque", listOf(
                CarGeneration("I (L538)", 2011, 2019),
                CarGeneration("II (L551)", 2018, 2025),
            )),
            CarModel("Range Rover Sport", listOf(
                CarGeneration("I (L320)", 2005, 2013),
                CarGeneration("II (L494)", 2013, 2022),
                CarGeneration("III (L461)", 2022, 2025),
            )),
        )),
        CarBrand("Lexus", R.drawable.logo_lexus, listOf(
            CarModel("ES", listOf(
                CarGeneration("V (XV30)", 2001, 2006),
                CarGeneration("VI (XV40)", 2006, 2012),
                CarGeneration("VII (XV60)", 2012, 2018),
                CarGeneration("VIII (XZ10)", 2018, 2025),
            )),
            CarModel("GX", listOf(
                CarGeneration("I (J120)", 2002, 2009),
                CarGeneration("II (J150)", 2009, 2025),
            )),
            CarModel("IS", listOf(
                CarGeneration("I (XE10)", 1998, 2005),
                CarGeneration("II (XE20)", 2005, 2013),
                CarGeneration("III (XE30)", 2013, 2025),
            )),
            CarModel("LX", listOf(
                CarGeneration("II (UZJ100)", 1998, 2007),
                CarGeneration("III (URJ200)", 2007, 2021),
                CarGeneration("IV (VJA310)", 2021, 2025),
            )),
            CarModel("NX", listOf(
                CarGeneration("I (AZ10)", 2014, 2021),
                CarGeneration("II (AZ20)", 2021, 2025),
            )),
            CarModel("RX", listOf(
                CarGeneration("I (XU10)", 1998, 2003),
                CarGeneration("II (XU30)", 2003, 2008),
                CarGeneration("III (AL10)", 2008, 2015),
                CarGeneration("IV (AL20)", 2015, 2022),
                CarGeneration("V (AL30)", 2022, 2025),
            )),
        )),
        CarBrand("Mazda", R.drawable.logo_mazda, listOf(
            CarModel("2 / Demio", listOf(
                CarGeneration("II (DY)", 2002, 2007),
                CarGeneration("III (DE)", 2007, 2014),
                CarGeneration("IV (DJ)", 2014, 2025),
            )),
            CarModel("3", listOf(
                CarGeneration("BK", 2003, 2009),
                CarGeneration("BL", 2009, 2013),
                CarGeneration("BM", 2013, 2019),
                CarGeneration("BP", 2019, 2025),
            )),
            CarModel("5", listOf(
                CarGeneration("I (CR)", 2005, 2010),
                CarGeneration("II (CW)", 2010, 2018),
            )),
            CarModel("6", listOf(
                CarGeneration("GG", 2002, 2008),
                CarGeneration("GH", 2007, 2012),
                CarGeneration("GJ", 2012, 2025),
            )),
            CarModel("CX-3", listOf(CarGeneration("DK", 2015, 2025))),
            CarModel("CX-5", listOf(
                CarGeneration("KE", 2011, 2017),
                CarGeneration("KF", 2016, 2025),
            )),
            CarModel("CX-7", listOf(CarGeneration("ER", 2006, 2012))),
            CarModel("CX-9", listOf(
                CarGeneration("I (TB)", 2006, 2015),
                CarGeneration("II (TC)", 2015, 2025),
            )),
            CarModel("MX-5", listOf(
                CarGeneration("NB", 1998, 2005),
                CarGeneration("NC", 2005, 2015),
                CarGeneration("ND", 2015, 2025),
            )),
        )),
        CarBrand("Mercedes-Benz", R.drawable.logo_mercedes, listOf(
            CarModel("A-класс", listOf(
                CarGeneration("W168", 1997, 2004),
                CarGeneration("W169", 2004, 2012),
                CarGeneration("W176", 2012, 2018),
                CarGeneration("W177", 2018, 2025),
            )),
            CarModel("B-класс", listOf(
                CarGeneration("W245", 2005, 2011),
                CarGeneration("W246", 2011, 2018),
                CarGeneration("W247", 2018, 2025),
            )),
            CarModel("C-класс", listOf(
                CarGeneration("W202", 1993, 2001),
                CarGeneration("W203", 2000, 2007),
                CarGeneration("W204", 2007, 2014),
                CarGeneration("W205", 2014, 2021),
                CarGeneration("W206", 2021, 2025),
            )),
            CarModel("CLA", listOf(
                CarGeneration("C117", 2013, 2019),
                CarGeneration("C118", 2019, 2025),
            )),
            CarModel("E-класс", listOf(
                CarGeneration("W210", 1995, 2003),
                CarGeneration("W211", 2002, 2009),
                CarGeneration("W212", 2009, 2016),
                CarGeneration("W213", 2016, 2023),
                CarGeneration("W214", 2023, 2025),
            )),
            CarModel("GLA", listOf(
                CarGeneration("X156", 2013, 2019),
                CarGeneration("H247", 2019, 2025),
            )),
            CarModel("GLB", listOf(CarGeneration("X247", 2019, 2025))),
            CarModel("GLC", listOf(
                CarGeneration("X253", 2015, 2022),
                CarGeneration("X254", 2022, 2025),
            )),
            CarModel("GLE", listOf(
                CarGeneration("W166", 2015, 2019),
                CarGeneration("V167", 2018, 2025),
            )),
            CarModel("GLS", listOf(
                CarGeneration("X166", 2015, 2019),
                CarGeneration("X167", 2019, 2025),
            )),
            CarModel("S-класс", listOf(
                CarGeneration("W140", 1991, 1998),
                CarGeneration("W220", 1998, 2005),
                CarGeneration("W221", 2005, 2013),
                CarGeneration("W222", 2013, 2020),
                CarGeneration("W223", 2020, 2025),
            )),
            CarModel("Vito / V-класс", listOf(
                CarGeneration("W639", 2003, 2014),
                CarGeneration("W447", 2014, 2025),
            )),
        )),
        CarBrand("Mitsubishi", R.drawable.logo_mitsubishi, listOf(
            CarModel("ASX", listOf(CarGeneration("I", 2010, 2023))),
            CarModel("Eclipse Cross", listOf(CarGeneration("I", 2017, 2025))),
            CarModel("L200", listOf(
                CarGeneration("IV", 2005, 2015),
                CarGeneration("V", 2015, 2025),
            )),
            CarModel("Lancer", listOf(
                CarGeneration("IX", 2003, 2010),
                CarGeneration("X", 2007, 2017),
            )),
            CarModel("Outlander", listOf(
                CarGeneration("I (CU)", 2001, 2008),
                CarGeneration("II (CW)", 2006, 2012),
                CarGeneration("III (GF)", 2012, 2021),
                CarGeneration("IV", 2021, 2025),
            )),
            CarModel("Pajero", listOf(
                CarGeneration("III", 1999, 2006),
                CarGeneration("IV", 2006, 2021),
            )),
            CarModel("Pajero Sport", listOf(
                CarGeneration("II", 2008, 2016),
                CarGeneration("III", 2015, 2025),
            )),
        )),
        CarBrand("Nissan", R.drawable.logo_nissan, listOf(
            CarModel("Almera", listOf(
                CarGeneration("N15", 1995, 2000),
                CarGeneration("N16", 2000, 2006),
                CarGeneration("G15", 2012, 2018),
            )),
            CarModel("Juke", listOf(
                CarGeneration("I (F15)", 2010, 2019),
                CarGeneration("II (F16)", 2019, 2025),
            )),
            CarModel("Murano", listOf(
                CarGeneration("I (Z50)", 2002, 2008),
                CarGeneration("II (Z51)", 2007, 2014),
                CarGeneration("III (Z52)", 2014, 2025),
            )),
            CarModel("Note", listOf(
                CarGeneration("I (E11)", 2004, 2013),
                CarGeneration("II (E12)", 2012, 2020),
                CarGeneration("III (E13)", 2020, 2025),
            )),
            CarModel("Pathfinder", listOf(
                CarGeneration("III (R51)", 2004, 2013),
                CarGeneration("IV (R52)", 2012, 2021),
                CarGeneration("V (R53)", 2021, 2025),
            )),
            CarModel("Qashqai", listOf(
                CarGeneration("I (J10)", 2006, 2013),
                CarGeneration("II (J11)", 2013, 2021),
                CarGeneration("III (J12)", 2021, 2025),
            )),
            CarModel("Teana", listOf(
                CarGeneration("I (J31)", 2003, 2008),
                CarGeneration("II (J32)", 2008, 2014),
                CarGeneration("III (L33)", 2014, 2020),
            )),
            CarModel("Tiida", listOf(
                CarGeneration("I (C11)", 2004, 2012),
                CarGeneration("II (C13)", 2011, 2018),
            )),
            CarModel("X-Trail", listOf(
                CarGeneration("I (T30)", 2000, 2007),
                CarGeneration("II (T31)", 2007, 2013),
                CarGeneration("III (T32)", 2013, 2021),
                CarGeneration("IV (T33)", 2021, 2025),
            )),
        )),
        CarBrand("Opel", R.drawable.logo_opel, listOf(
            CarModel("Astra", listOf(
                CarGeneration("G", 1998, 2005),
                CarGeneration("H", 2004, 2014),
                CarGeneration("J", 2009, 2015),
                CarGeneration("K", 2015, 2022),
            )),
            CarModel("Corsa", listOf(
                CarGeneration("C", 2000, 2006),
                CarGeneration("D", 2006, 2014),
                CarGeneration("E", 2014, 2019),
                CarGeneration("F", 2019, 2025),
            )),
            CarModel("Insignia", listOf(
                CarGeneration("I", 2008, 2017),
                CarGeneration("II", 2017, 2022),
            )),
            CarModel("Meriva", listOf(
                CarGeneration("A", 2003, 2010),
                CarGeneration("B", 2010, 2017),
            )),
            CarModel("Mokka", listOf(
                CarGeneration("I", 2012, 2020),
                CarGeneration("II", 2020, 2025),
            )),
            CarModel("Zafira", listOf(
                CarGeneration("A", 1999, 2005),
                CarGeneration("B", 2005, 2014),
                CarGeneration("C Tourer", 2011, 2019),
            )),
        )),
        CarBrand("Peugeot", R.drawable.logo_peugeot, listOf(
            CarModel("206", listOf(CarGeneration("I", 1998, 2012))),
            CarModel("207", listOf(CarGeneration("I", 2006, 2014))),
            CarModel("208", listOf(
                CarGeneration("I", 2012, 2019),
                CarGeneration("II", 2019, 2025),
            )),
            CarModel("301", listOf(CarGeneration("I", 2012, 2021))),
            CarModel("308", listOf(
                CarGeneration("I (T7)", 2007, 2015),
                CarGeneration("II (T9)", 2013, 2021),
                CarGeneration("III (P5)", 2021, 2025),
            )),
            CarModel("408", listOf(
                CarGeneration("I", 2010, 2017),
                CarGeneration("II", 2022, 2025),
            )),
            CarModel("508", listOf(
                CarGeneration("I", 2010, 2018),
                CarGeneration("II", 2018, 2025),
            )),
            CarModel("2008", listOf(
                CarGeneration("I", 2013, 2019),
                CarGeneration("II", 2019, 2025),
            )),
            CarModel("3008", listOf(
                CarGeneration("I", 2009, 2016),
                CarGeneration("II", 2016, 2025),
            )),
            CarModel("5008", listOf(
                CarGeneration("I", 2009, 2017),
                CarGeneration("II", 2017, 2025),
            )),
        )),
        CarBrand("Porsche", R.drawable.logo_porsche, listOf(
            CarModel("Cayenne", listOf(
                CarGeneration("I (955)", 2002, 2010),
                CarGeneration("II (958)", 2010, 2018),
                CarGeneration("III (9YA)", 2017, 2025),
            )),
            CarModel("Macan", listOf(
                CarGeneration("I (95B)", 2013, 2025),
            )),
            CarModel("Panamera", listOf(
                CarGeneration("I (970)", 2009, 2016),
                CarGeneration("II (971)", 2016, 2025),
            )),
            CarModel("911", listOf(
                CarGeneration("996", 1997, 2005),
                CarGeneration("997", 2004, 2012),
                CarGeneration("991", 2011, 2019),
                CarGeneration("992", 2018, 2025),
            )),
        )),
        CarBrand("Renault", R.drawable.logo_renault, listOf(
            CarModel("Arkana", listOf(CarGeneration("I", 2019, 2025))),
            CarModel("Captur", listOf(
                CarGeneration("I", 2013, 2019),
                CarGeneration("II", 2019, 2025),
            )),
            CarModel("Duster", listOf(
                CarGeneration("I", 2010, 2017),
                CarGeneration("I рестайлинг", 2015, 2020),
                CarGeneration("II", 2020, 2025),
            )),
            CarModel("Fluence", listOf(CarGeneration("I", 2009, 2017))),
            CarModel("Kaptur", listOf(
                CarGeneration("I", 2016, 2020),
                CarGeneration("I рестайлинг", 2020, 2025),
            )),
            CarModel("Koleos", listOf(
                CarGeneration("I", 2007, 2016),
                CarGeneration("II", 2016, 2025),
            )),
            CarModel("Logan", listOf(
                CarGeneration("I", 2004, 2012),
                CarGeneration("II", 2012, 2020),
            )),
            CarModel("Megane", listOf(
                CarGeneration("II", 2002, 2009),
                CarGeneration("III", 2008, 2016),
                CarGeneration("IV", 2015, 2023),
            )),
            CarModel("Sandero", listOf(
                CarGeneration("I", 2007, 2014),
                CarGeneration("II", 2012, 2020),
            )),
            CarModel("Scenic", listOf(
                CarGeneration("II", 2003, 2009),
                CarGeneration("III", 2009, 2016),
                CarGeneration("IV", 2016, 2022),
            )),
        )),
        CarBrand("Skoda", R.drawable.logo_skoda, listOf(
            CarModel("Fabia", listOf(
                CarGeneration("I (6Y)", 1999, 2007),
                CarGeneration("II (5J)", 2007, 2014),
                CarGeneration("III (NJ)", 2014, 2021),
                CarGeneration("IV (PJ)", 2021, 2025),
            )),
            CarModel("Karoq", listOf(CarGeneration("I (NU)", 2017, 2025))),
            CarModel("Kodiaq", listOf(CarGeneration("I (NS)", 2016, 2025))),
            CarModel("Octavia", listOf(
                CarGeneration("I (1U)", 1996, 2010),
                CarGeneration("II (1Z)", 2004, 2013),
                CarGeneration("III (5E)", 2012, 2020),
                CarGeneration("IV (NX)", 2019, 2025),
            )),
            CarModel("Rapid", listOf(
                CarGeneration("I (NH)", 2012, 2019),
                CarGeneration("II (NK)", 2020, 2025),
            )),
            CarModel("Superb", listOf(
                CarGeneration("I (3U)", 2001, 2008),
                CarGeneration("II (3T)", 2008, 2015),
                CarGeneration("III (3V)", 2015, 2025),
            )),
            CarModel("Yeti", listOf(CarGeneration("I (5L)", 2009, 2018))),
        )),
        CarBrand("Subaru", R.drawable.logo_subaru, listOf(
            CarModel("Forester", listOf(
                CarGeneration("I (SF)", 1997, 2002),
                CarGeneration("II (SG)", 2002, 2008),
                CarGeneration("III (SH)", 2007, 2013),
                CarGeneration("IV (SJ)", 2012, 2018),
                CarGeneration("V (SK)", 2018, 2025),
            )),
            CarModel("Impreza", listOf(
                CarGeneration("II (GD)", 2000, 2007),
                CarGeneration("III (GE)", 2007, 2011),
                CarGeneration("IV (GJ)", 2011, 2016),
                CarGeneration("V (GT)", 2016, 2023),
            )),
            CarModel("Legacy", listOf(
                CarGeneration("IV (BL)", 2003, 2009),
                CarGeneration("V (BM)", 2009, 2014),
                CarGeneration("VI (BN)", 2014, 2020),
                CarGeneration("VII (BW)", 2019, 2025),
            )),
            CarModel("Outback", listOf(
                CarGeneration("III (BP)", 2003, 2009),
                CarGeneration("IV (BR)", 2009, 2014),
                CarGeneration("V (BS)", 2014, 2020),
                CarGeneration("VI (BT)", 2019, 2025),
            )),
            CarModel("XV / Crosstrek", listOf(
                CarGeneration("I (GP)", 2011, 2017),
                CarGeneration("II (GT)", 2017, 2025),
            )),
            CarModel("WRX", listOf(
                CarGeneration("I (VA)", 2014, 2021),
                CarGeneration("II (VB)", 2021, 2025),
            )),
        )),
        CarBrand("Suzuki", R.drawable.logo_suzuki, listOf(
            CarModel("Grand Vitara", listOf(
                CarGeneration("II (JT)", 2005, 2015),
                CarGeneration("III", 2015, 2025),
            )),
            CarModel("Jimny", listOf(
                CarGeneration("III (JB43)", 1998, 2018),
                CarGeneration("IV (JB64)", 2018, 2025),
            )),
            CarModel("SX4", listOf(
                CarGeneration("I", 2006, 2014),
                CarGeneration("II (S-Cross)", 2013, 2025),
            )),
            CarModel("Swift", listOf(
                CarGeneration("III", 2004, 2010),
                CarGeneration("IV", 2010, 2017),
                CarGeneration("V", 2017, 2025),
            )),
            CarModel("Vitara", listOf(
                CarGeneration("IV (LY)", 2014, 2025),
            )),
        )),
        CarBrand("Toyota", R.drawable.logo_toyota, listOf(
            CarModel("Alphard", listOf(
                CarGeneration("I (H10)", 2002, 2008),
                CarGeneration("II (H20)", 2008, 2015),
                CarGeneration("III (H30)", 2015, 2025),
            )),
            CarModel("Avensis", listOf(
                CarGeneration("II (T250)", 2003, 2008),
                CarGeneration("III (T270)", 2008, 2018),
            )),
            CarModel("Camry", listOf(
                CarGeneration("XV30", 2001, 2006),
                CarGeneration("XV40", 2006, 2011),
                CarGeneration("XV50", 2011, 2017),
                CarGeneration("XV70", 2017, 2025),
            )),
            CarModel("C-HR", listOf(CarGeneration("I", 2016, 2025))),
            CarModel("Corolla", listOf(
                CarGeneration("E120", 2000, 2007),
                CarGeneration("E140", 2006, 2013),
                CarGeneration("E160", 2012, 2019),
                CarGeneration("E210", 2018, 2025),
            )),
            CarModel("Fortuner", listOf(
                CarGeneration("I (AN50)", 2004, 2015),
                CarGeneration("II (AN160)", 2015, 2025),
            )),
            CarModel("Highlander", listOf(
                CarGeneration("I (U20)", 2000, 2007),
                CarGeneration("II (U40)", 2007, 2013),
                CarGeneration("III (U50)", 2013, 2019),
                CarGeneration("IV (U60)", 2019, 2025),
            )),
            CarModel("Hilux", listOf(
                CarGeneration("VII (AN10)", 2004, 2015),
                CarGeneration("VIII (AN120)", 2015, 2025),
            )),
            CarModel("Land Cruiser 200", listOf(CarGeneration("200", 2007, 2021))),
            CarModel("Land Cruiser 300", listOf(CarGeneration("300", 2021, 2025))),
            CarModel("Land Cruiser Prado", listOf(
                CarGeneration("120", 2002, 2009),
                CarGeneration("150", 2009, 2025),
            )),
            CarModel("RAV4", listOf(
                CarGeneration("II (XA20)", 2000, 2005),
                CarGeneration("III (XA30)", 2005, 2012),
                CarGeneration("IV (XA40)", 2012, 2019),
                CarGeneration("V (XA50)", 2018, 2025),
            )),
            CarModel("Yaris", listOf(
                CarGeneration("I (P1)", 1999, 2005),
                CarGeneration("II (P9)", 2005, 2011),
                CarGeneration("III (P13)", 2011, 2020),
                CarGeneration("IV", 2020, 2025),
            )),
        )),
        CarBrand("Volkswagen", R.drawable.logo_vw, listOf(
            CarModel("Amarok", listOf(
                CarGeneration("I", 2010, 2022),
                CarGeneration("II", 2022, 2025),
            )),
            CarModel("Arteon", listOf(CarGeneration("I", 2017, 2025))),
            CarModel("Caddy", listOf(
                CarGeneration("III", 2004, 2015),
                CarGeneration("IV", 2015, 2020),
                CarGeneration("V", 2020, 2025),
            )),
            CarModel("Golf", listOf(
                CarGeneration("IV", 1997, 2006),
                CarGeneration("V", 2003, 2009),
                CarGeneration("VI", 2008, 2013),
                CarGeneration("VII", 2012, 2020),
                CarGeneration("VIII", 2019, 2025),
            )),
            CarModel("ID.4", listOf(CarGeneration("I", 2020, 2025))),
            CarModel("Jetta", listOf(
                CarGeneration("V", 2005, 2011),
                CarGeneration("VI", 2010, 2018),
                CarGeneration("VII", 2018, 2025),
            )),
            CarModel("Passat", listOf(
                CarGeneration("B5", 1996, 2005),
                CarGeneration("B6", 2005, 2010),
                CarGeneration("B7", 2010, 2014),
                CarGeneration("B8", 2014, 2023),
                CarGeneration("B9", 2023, 2025),
            )),
            CarModel("Polo", listOf(
                CarGeneration("IV (9N)", 2001, 2009),
                CarGeneration("V (6R)", 2009, 2017),
                CarGeneration("VI", 2017, 2025),
            )),
            CarModel("T-Cross", listOf(CarGeneration("I", 2018, 2025))),
            CarModel("T-Roc", listOf(CarGeneration("I", 2017, 2025))),
            CarModel("Taos", listOf(CarGeneration("I", 2020, 2025))),
            CarModel("Tiguan", listOf(
                CarGeneration("I (5N)", 2007, 2016),
                CarGeneration("II (AD)", 2016, 2025),
            )),
            CarModel("Touareg", listOf(
                CarGeneration("I (7L)", 2002, 2010),
                CarGeneration("II (7P)", 2010, 2018),
                CarGeneration("III (CR)", 2018, 2025),
            )),
            CarModel("Transporter", listOf(
                CarGeneration("T5", 2003, 2015),
                CarGeneration("T6", 2015, 2025),
            )),
        )),
        CarBrand("Volvo", R.drawable.logo_volvo, listOf(
            CarModel("S40", listOf(CarGeneration("II", 2004, 2012))),
            CarModel("S60", listOf(
                CarGeneration("I", 2000, 2009),
                CarGeneration("II", 2010, 2018),
                CarGeneration("III", 2018, 2025),
            )),
            CarModel("S80", listOf(
                CarGeneration("I", 1998, 2006),
                CarGeneration("II", 2006, 2016),
            )),
            CarModel("S90", listOf(CarGeneration("II", 2016, 2025))),
            CarModel("V40", listOf(CarGeneration("II", 2012, 2019))),
            CarModel("V60", listOf(
                CarGeneration("I", 2010, 2018),
                CarGeneration("II", 2018, 2025),
            )),
            CarModel("V90", listOf(CarGeneration("II", 2016, 2025))),
            CarModel("XC40", listOf(CarGeneration("I", 2017, 2025))),
            CarModel("XC60", listOf(
                CarGeneration("I", 2008, 2017),
                CarGeneration("II", 2017, 2025),
            )),
            CarModel("XC90", listOf(
                CarGeneration("I", 2002, 2014),
                CarGeneration("II", 2014, 2025),
            )),
        )),
    )

    fun brandNames(): List<String> = brands.map { it.name }

    fun modelsFor(brandName: String): List<CarModel> =
        brands.firstOrNull { it.name == brandName }?.models ?: emptyList()

    fun generationsFor(brandName: String, modelName: String): List<CarGeneration> =
        modelsFor(brandName).firstOrNull { it.name == modelName }?.generations ?: emptyList()

    fun logoFor(brandName: String): Int =
        brands.firstOrNull { it.name == brandName }?.logoRes ?: R.drawable.logo_default

}
