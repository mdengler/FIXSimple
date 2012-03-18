package com.martindengler.proj.FIXSimple.spec;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;


public enum Tag {

    // NOTE -- Header and Trailer enums should be first and last,
    // respectively, so their "natural" order (Java enum terminology)
    // matches the wire order, so FIXMessage.toWire() operates
    // correctly.  Don't change the order of these enum declarations
    // unless you want to re-implement FIXMessage.toWire() to maintain
    // correctness.
    //
    // Header tags
        BEGINSTRING(8),
        BODYLENGTH(9),
        MSGTYPE(35), // Always unencrypted, must be third field in message
    // NOTE -- Header and Trailer enums should be first and last,
    // respectively, so their "natural" order (Java enum terminology)
    // matches the wire order, so FIXMessage.toWire() operates
    // correctly.  Don't change the order of these enum declarations
    // unless you want to re-implement FIXMessage.toWire() to maintain
    // correctness.


        ACCOUNT(1),
        ACCRUEDINTERESTAMT(159),
        ACCRUEDINTERESTRATE(158),
        ADJUSTMENT(334),
        ADVID(2),
        ADVREFID(3),
        ADVSIDE(4),
        ADVTRANSTYPE(5),
        AGGREGATEDBOOK(266),
        ALLOCACCOUNT(79),
        ALLOCAVGPX(153),
        ALLOCHANDLINST(209),
        ALLOCID(70),
        ALLOCLINKID(196),
        ALLOCLINKTYPE(197),
        ALLOCNETMONEY(154),
        ALLOCPRICE(366),
        ALLOCREJCODE(88),
        ALLOCSHARES(80),
        ALLOCSTATUS(87),
        ALLOCTEXT(161),
        ALLOCTRANSTYPE(71),
        AVGPRXPRECISION(74),
        AVGPX(6),
        BASISPXTYPE(419),
        BEGINSEQNO(7),
        BENCHMARK(219),
        BIDDESCRIPTOR(400),
        BIDDESCRIPTORTYPE(399),
        BIDFORWARDPOINTS(189),
        BIDID(390),
        BIDPX(132),
        BIDREQUESTTRANSTYPE(374),
        BIDSIZE(134),
        BIDSPOTRATE(188),
        BIDTYPE(394),
        BROKEROFCREDIT(92),
        BUSINESSREJECTREASON(380),
        BUSINESSREJECTREFID(379),
        BUYVOLUME(330),
        CASHORDERQTY(152),
        CASHSETTLAGENTACCTNAME(185),
        CASHSETTLAGENTACCTNUM(184),
        CASHSETTLAGENTCODE(183),
        CASHSETTLAGENTCONTACTNAME(186),
        CASHSETTLAGENTCONTACTPHONE(187),
        CASHSETTLAGENTNAME(182),
        CLEARINGACCOUNT(440),
        CLEARINGFIRM(439),
        CLIENTBIDID(391),
        CLIENTID(109),
        CLORDID(11),
        COMMISSION(12),
        COMMTYPE(13), // (aka OrderCapacity)
        COMPLIANCEID(376),
        CONTRABROKER(375),
        CONTRACTMULTIPLIER(231),
        CONTRATRADEQTY(437),
        CONTRATRADER(337),
        CONTRATRADETIME(438),
        CORPORATEACTION(292),
        COUNTRY(421),
        COUPONRATE(223),
        COVEREDORUNCOVERED(203),
        CROSSPERCENT(413),
        CUMQTY(14),
        CURRENCY(15),
        CUSTOMERORFIRM(204),
        CXLQTY(84),
        CXLREJREASON(102),
        CXLREJRESPONSETO(434),
        CXLTYPE(125), // (no longer used)
        DAYAVGPX(426),
        DAYCUMQTY(425),
        DAYORDERQTY(424),
        DEFBIDSIZE(293),
        DEFOFFERSIZE(294),
        DELETEREASON(285),
        DELIVERTOLOCATIONID(145),
        DELIVERTOSUBID(129),
        DESKID(284),
        DISCRETIONINST(388),
        DISCRETIONOFFSET(389),
        DKREASON(127),
        DLVYINST(86), // (no longer used)
        DUETORELATED(329),
        EFFECTIVETIME(168),
        EFPTRACKINGERROR(405),
        EMAILTHREADID(164),
        EMAILTYPE(94),
        ENCODEDALLOCTEXT(361),
        ENCODEDALLOCTEXTLEN(360),
        ENCODEDHEADLINE(359),
        ENCODEDHEADLINELEN(358),
        ENCODEDISSUER(349),
        ENCODEDISSUERLEN(348),
        ENCODEDLISTEXECINST(353),
        ENCODEDLISTEXECINSTLEN(352),
        ENCODEDSECURITYDESC(351),
        ENCODEDSECURITYDESCLEN(350),
        ENCODEDSUBJECT(357),
        ENCODEDSUBJECTLEN(356),
        ENCODEDTEXT(355),
        ENCODEDTEXTLEN(354),
        ENCODEDUNDERLYINGISSUER(363),
        ENCODEDUNDERLYINGISSUERLEN(362),
        ENCODEDUNDERLYINGSECURITYDESC(365),
        ENCODEDUNDERLYINGSECURITYDESCLEN(364),
        ENCRYPTMETHOD(98),
        ENDSEQNO(16),
        EXCHANGEFORPHYSICAL(411),
        EXDESTINATION(100),
        EXECBROKER(76),
        EXECID(17),
        EXECINST(18),
        EXECREFID(19),
        EXECRESTATEMENTREASON(378),
        EXECTRANSTYPE(20),
        EXECTYPE(150),
        EXPIREDATE(432),
        EXPIRETIME(126),
        FAIRVALUE(406),
        FINANCIALSTATUS(291),
        FOREXREQ(121),
        FUTSETTDATE2(193),
        FUTSETTDATE(64),
        GAPFILLFLAG(123),
        GROSSTRADEAMT(381),
        GTBOOKINGINST(427),
        HALTREASON(327),
        HANDLINST(21),
        HEADLINE(148),
        HEARTBTINT(108),
        HIGHPX(332),
        IDSOURCE(22),
        INCTAXIND(416),
        INVIEWOFCOMMON(328),
        IOIID(23),
        IOINATURALFLAG(130),
        IOIOTHSVC(24), // (no longer used)
        IOIQLTYIND(25),
        IOIQUALIFIER(104),
        IOIREFID(26),
        IOISHARES(27),
        IOITRANSTYPE(28),
        ISSUER(106),
        LASTCAPACITY(29),
        LASTFORWARDPOINTS(195),
        LASTMKT(30),
        LASTMSGSEQNUMPROCESSED(369),
        LASTPX(31),
        LASTSHARES(32),
        LASTSPOTRATE(194),
        LEAVESQTY(151),
        LINESOFTEXT(33),
        LIQUIDITYINDTYPE(409),
        LIQUIDITYNUMSECURITIES(441),
        LIQUIDITYPCTHIGH(403),
        LIQUIDITYPCTLOW(402),
        LIQUIDITYVALUE(404),
        LISTEXECINST(69),
        LISTEXECINSTTYPE(433),
        LISTID(66),
        LISTNAME(392),
        LISTORDERSTATUS(431),
        LISTSEQNO(67),
        LISTSTATUSENCODEDTEXT(446),
        LISTSTATUSENCODEDTEXTLEN(445),
        LISTSTATUSTEXT(444),
        LISTSTATUSTYPE(429),
        LOCATEREQD(114),
        LOCATIONID(283),
        LOWPX(333),
        MARKETDEPTH(264),
        MATURITYDAY(205),
        MATURITYMONTHYEAR(200),
        MAXFLOOR(111),
        MAXMESSAGESIZE(383),
        MAXSHOW(210),
        MDENTRYBUYER(288),
        MDENTRYDATE(272),
        MDENTRYID(278),
        MDENTRYORIGINATOR(282),
        MDENTRYPOSITIONNO(290),
        MDENTRYPX(270),
        MDENTRYREFID(280),
        MDENTRYSELLER(289),
        MDENTRYSIZE(271),
        MDENTRYTIME(273),
        MDENTRYTYPE(269),
        MDMKT(275),
        MDREQID(262),
        MDREQREJREASON(281),
        MDUPDATEACTION(279),
        MDUPDATETYPE(265),
        MESSAGEENCODING(347),
        MINQTY(110),
        MISCFEEAMT(137),
        MISCFEECURR(138),
        MISCFEETYPE(139),
        MSGDIRECTION(385),
        MSGSEQNUM(34),
        MULTILEGREPORTINGTYPE(442),
        NETGROSSIND(430),
        NETMONEY(118),
        NEWSEQNO(36),
        NOALLOCS(78),
        NOBIDCOMPONENTS(420),
        NOBIDDESCRIPTORS(398),
        NOCONTRABROKERS(382),
        NODLVYINST(85), // (no longer used)
        NOEXECS(124),
        NOIOIQUALIFIERS(199),
        NOMDENTRIES(268),
        NOMDENTRYTYPES(267),
        NOMISCFEES(136),
        NOMSGTYPES(384),
        NOORDERS(73),
        NOQUOTEENTRIES(295),
        NOQUOTESETS(296),
        NORELATEDSYM(146),
        NOROUTINGIDS(215),
        NORPTS(82),
        NOSTRIKES(428),
        NOT_DEFINED(101),
        NOTIFYBROKEROFCREDIT(208),
        NOTRADINGSESSIONS(386),
        NUMBEROFORDERS(346),
        NUMBIDDERS(417),
        NUMDAYSINTEREST(157),
        NUMTICKETS(395),
        OFFERFORWARDPOINTS(191),
        OFFERPX(133),
        OFFERSIZE(135),
        OFFERSPOTRATE(190),
        ONBEHALFOFCOMPID(115),
        ONBEHALFOFLOCATIONID(144),
        ONBEHALFOFSENDINGTIME(370),
        ONBEHALFOFSUBID(116),
        OPENCLOSE(77),
        OPENCLOSESETTLEFLAG(286),
        OPTATTRIBUTE(206),
        ORDERID(37),
        ORDERQTY2(192),
        ORDERQTY(38),
        ORDREJREASON(103),
        ORDSTATUS(39),
        ORDTYPE(40),
        ORIGCLORDID(41),
        ORIGSENDINGTIME(122),
        ORIGTIME(42),
        OUTMAINCNTRYUINDEX(412),
        OUTSIDEINDEXPCT(407),
        PEGDIFFERENCE(211),
        POSSDUPFLAG(43),
        POSSRESEND(97),
        PREVCLOSEPX(140),
        PRICE(44),
        PRICETYPE(423),
        PROCESSCODE(81),
        PROGPERIODINTERVAL(415),
        PROGRPTREQS(414),
        PUTORCALL(201),
        QUOTEACKSTATUS(297),
        QUOTECANCELTYPE(298),
        QUOTECONDITION(276),
        QUOTEENTRYID(299),
        QUOTEENTRYREJECTREASON(368),
        QUOTEID(117),
        QUOTEREJECTREASON(300),
        QUOTEREQID(131),
        QUOTEREQUESTTYPE(303),
        QUOTERESPONSELEVEL(301),
        QUOTESETID(302),
        QUOTESETVALIDUNTILTIME(367),
        RATIOQTY(319),
        RAWDATA(96),
        RAWDATALENGTH(95),
        REFALLOCID(72),
        REFMSGTYPE(372),
        REFSEQNUM(45),
        REFTAGID(371),
        RELATDSYM(46),
        REPORTTOEXCH(113),
        RESERVED_ALLOCATED_TO_THE_FIXED_INCOME_PROPOSAL_220(220),
        RESERVED_ALLOCATED_TO_THE_FIXED_INCOME_PROPOSAL_221(221),
        RESERVED_ALLOCATED_TO_THE_FIXED_INCOME_PROPOSAL_222(222),
        RESERVED_ALLOCATED_TO_THE_FIXED_INCOME_PROPOSAL_224(224),
        RESERVED_ALLOCATED_TO_THE_FIXED_INCOME_PROPOSAL_225(225),
        RESERVED_ALLOCATED_TO_THE_FIXED_INCOME_PROPOSAL_226(226),
        RESERVED_ALLOCATED_TO_THE_FIXED_INCOME_PROPOSAL_227(227),
        RESERVED_ALLOCATED_TO_THE_FIXED_INCOME_PROPOSAL_228(228),
        RESERVED_ALLOCATED_TO_THE_FIXED_INCOME_PROPOSAL_229(229),
        RESERVED_ALLOCATED_TO_THE_FIXED_INCOME_PROPOSAL_230(230),
        RESETSEQNUMFLAG(141),
        ROUTINGID(217),
        ROUTINGTYPE(216),
        RPTSEQ(83),
        RULE80A(47),
        SECONDARYORDERID(198),
        SECURITYDESC(107),
        SECURITYEXCHANGE(207),
        SECURITYID(48),
        SECURITYREQID(320),
        SECURITYREQUESTTYPE(321),
        SECURITYRESPONSEID(322),
        SECURITYRESPONSETYPE(323),
        SECURITYSETTLAGENTACCTNAME(179),
        SECURITYSETTLAGENTACCTNUM(178),
        SECURITYSETTLAGENTCODE(177),
        SECURITYSETTLAGENTCONTACTNAME(180),
        SECURITYSETTLAGENTCONTACTPHONE(181),
        SECURITYSETTLAGENTNAME(176),
        SECURITYSTATUSREQID(324),
        SECURITYTRADINGSTATUS(326),
        SECURITYTYPE(167),
        SELLERDAYS(287),
        SELLVOLUME(331),
        SENDERCOMPID(49),
        SENDERLOCATIONID(142),
        SENDERSUBID(50),
        SENDINGDATE(51), // (no longer used)
        SENDINGTIME(52),
        SESSIONREJECTREASON(373),
        SETTLBRKRCODE(174),
        SETTLCURRAMT(119),
        SETTLCURRENCY(120),
        SETTLCURRFXRATE(155),
        SETTLCURRFXRATECALC(156),
        SETTLDELIVERYTYPE(172),
        SETTLDEPOSITORYCODE(173),
        SETTLINSTCODE(175),
        SETTLINSTID(162),
        SETTLINSTMODE(160),
        SETTLINSTREFID(214),
        SETTLINSTSOURCE(165),
        SETTLINSTTRANSTYPE(163),
        SETTLLOCATION(166),
        SETTLMNTTYP(63),
        SETTLEMENTTYPE(63),  // previous abbreviation is an abomination
        SHARES(53),
        SIDE(54),
        SIDEVALUE1(396),
        SIDEVALUE2(397),
        SIDEVALUEIND(401),
        SOLICITEDFLAG(377),
        SPREADTOBENCHMARK(218),
        STANDINSTDBID(171),
        STANDINSTDBNAME(170),
        STANDINSTDBTYPE(169),
        STOPPX(99),
        STRIKEPRICE(202),
        STRIKETIME(443),
        SUBJECT(147),
        SUBSCRIPTIONREQUESTTYPE(263),
        SYMBOL(55),
        SYMBOLSFX(65),
        TARGETCOMPID(56),
        TARGETLOCATIONID(143),
        TARGETSUBID(57),
        TESTREQID(112),
        TEXT(58),
        TICKDIRECTION(274),
        TIMEINFORCE(59),
        TOTALNUMSECURITIES(393),
        TOTALVOLUMETRADED(387),
        TOTNOORDERS(68),  // (formerly ListNoOrds)
        TOTNOSTRIKES(422),
        TOTQUOTEENTRIES(304),
        TRADECONDITION(277),
        TRADEDATE(75),
        TRADETYPE(418),
        TRADINGSESSIONID(336),
        TRADSESCLOSETIME(344),
        TRADSESENDTIME(345),
        TRADSESMETHOD(338),
        TRADSESMODE(339),
        TRADSESOPENTIME(342),
        TRADSESPRECLOSETIME(343),
        TRADSESREQID(335),
        TRADSESSTARTTIME(341),
        TRADSESSTATUS(340),
        TRANSACTTIME(60),
        UNDERLYINGCONTRACTMULTIPLIER(436),
        UNDERLYINGCOUPONRATE(435),
        UNDERLYING_CURRENCY(318),
        UNDERLYINGIDSOURCE(305),
        UNDERLYINGISSUER(306),
        UNDERLYINGMATURITYDAY(314),
        UNDERLYINGMATURITYMONTHYEAR(313),
        UNDERLYINGOPTATTRIBUTE(317),
        UNDERLYINGPUTORCALL(315),
        UNDERLYINGSECURITYDESC(307),
        UNDERLYINGSECURITYEXCHANGE(308),
        UNDERLYINGSECURITYID(309),
        UNDERLYINGSECURITYTYPE(310),
        UNDERLYINGSTRIKEPRICE(316),
        UNDERLYINGSYMBOL(311),
        UNDERLYINGSYMBOLSFX(312),
        UNSOLICITEDINDICATOR(325),
        URGENCY(61),
        URLLINK(149),
        VALIDUNTILTIME(62),
        VALUEOFFUTURES(408),
        WAVENO(105),
        WTAVERAGELIQUIDITY(410),
        XMLDATA(213),
        XMLDATALEN(212),

    // NOTE -- Header and Trailer enums should be first and last,
    // respectively, so their "natural" order (Java enum terminology)
    // matches the wire order, so FIXMessage.toWire() operates
    // correctly.  Don't change the order of these enum declarations
    // unless you want to re-implement FIXMessage.toWire() to maintain
    // correctness.
        // Trailer tags
        SIGNATURELENGTH(93),
        SIGNATURE(89),
        CHECKSUM(10),
    // NOTE -- Header and Trailer enums should be first and last,
    // respectively, so their "natural" order (Java enum terminology)
    // matches the wire order, so FIXMessage.toWire() operates
    // correctly.  Don't change the order of these enum declarations
    // unless you want to re-implement FIXMessage.toWire() to maintain
    // correctness.

        //TODO: HopGroup component block tags

        ;

    private static final Map<Integer, Tag> lookup
        = new HashMap<Integer, Tag>();

    static {
        for(Tag t : EnumSet.allOf(Tag.class))
            lookup.put(t.getCode(), t);
    }

    private Integer code;

    public static Tag fromCode(Integer code) {
        return lookup.get(code);
    }

    private Tag(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return this.code;
    }

    public String toString() {
        return this.code.toString();
    }

    public String toString(Boolean verbose) {
        if (verbose)
            return this.toString() + "_" + this.name();
        else
            return this.toString();
    }

}
