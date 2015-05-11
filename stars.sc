s.options.numBuffers = 16000;
s.options.memSize = 655360;
s.boot;
s.freqscope;
s.plotTree;
s.scope;

~minang = 3500;
~maxang = 7500;

~emissioninst = {
	arg filename;
	var rows,fmin,fmax,out;
	
}

~csvtoinst = {
	arg filename;
	var rows,fmin,fmax,out;
	rows = CSVFileReader.read(filename); 
	rows.removeAt(0);
	rows = rows.collect {|v| v[1] = v[1].asFloat; v[2] = v[2].asFloat; };
	out = Dictionary.new();
	out["rows"] = rows;
	out["max"] = rows[rows.maxIndex{|v|v[2]}].[2];
	out["min"] = rows[rows.minIndex{|v|v[2]}].[2];
	out["fmax"] = rows[rows.maxIndex{|v|v[1]}].[1];
	out["fmin"] = rows[rows.minIndex{|v|v[1]}].[1];

	out
};



~midis = Dictionary["" -> 1, "A"->   1, "A "->  2, "A-"->  3, "A:"->  4, "A/"->  5, "A."->  6, "A("->  7, "A+"->  8, "A0"->  9, "A1"->  10, "A2"->  11, "A3"->  12, "A4"->  13, "A5"->  14, "A6"->  15, "A7"->  16, "A8"->  17, "A9"->  18, "Am"->  19, "Ap"->  20, "As"->  21, "AV"->  22, "B"->   23, "B "->  24, "B:"->  25, "B/"->  26, "B."->  27, "B+"->  28, "B0"->  29, "B1"->  30, "B2"->  31, "B3"->  32, "B4"->  33, "B5"->  34, "B6"->  35, "B7"->  36, "B8"->  37, "B9"->  38, "Be"->  39, "Bn"->  40, "Bp"->  41, "C"->   42, "C."->  43, "C("->  44, "C+"->  45, "C0"->  46, "C2"->  47, "C3"->  48, "C4"->  49, "C5"->  50, "C6"->  51, "C7"->  52, "C8"->  53, "C9"->  54, "Ce"->  55, "CH"->  56, "CI"->  57, "Cm"->  58, "Cp"->  59, "CV"->  60, "DA"->  61, "DB"->  62, "DC"->  63, "dF"->  64, "DF"->  65, "dG"->  66, "DG"->  67, "dK"->  68, "dM"->  69, "DQ"->  70, "DX"->  71, "DZ"->  72, "f"->   73, "f-"->  74, "F"->   75, "F:"->  76, "F."->  77, "F0"->  78, "F1"->  79, "F2"->  80, "F3"->  81, "F4"->  82, "F5"->  83, "F6"->  84, "F7"->  85, "F8"->  86, "F9"->  87, "Fm"->  88, "FO"->  89, "Fp"->  90, "FV"->  91, "Fw"->  92, "g"->   93, "g-"->  94, "G"->   95, "(G"->  96, "G "->  97, "G-"->  98, "G:"->  99, "G/"->  100, "G."->  101, "G("->  102, "G+"->  103, "G0"->  104, "G1"->  105, "G2"->  106, "G3"->  107, "G4"->  108, "G5"->  109, "G6"->  110, "G7"->  111, "G8"->  112, "G9"->  113, "Ge"->  114, "GI"->  115, "Gp"->  116, "GP"->  117, "GV"->  118, "Gw"->  119, "k"->   120, "k-"->  121, "K"->   122, "K-"->  123, "K:"->  124, "K("->  125, "K0"->  126, "K1"->  127, "K2"->   1, "K3"->   2, "K4"->   3, "K5"->   4, "K6"->   5, "K7"->   6, "K8"->   7, "K9"->   8, "Ke"->   9, "KI"->   10, "Kp"->   11, "m"->    12, "m+"->   13, "M"->    14, "M:"->   15, "M."->   16, "M0"->   17, "M1"->   18, "M2"->   19, "M3"->   20, "M4"->   21, "M5"->   22, "M6"->   23, "M7"->   24, "M8"->   25, "M9"->   26, "Ma"->   27, "Mb"->   28, "Mc"->   29, "Md"->   30, "Me"->   31, "MI"->   32, "Mp"->   33, "N"->    34, "N."->   35, "N0"->   36, "N2"->   37, "N3"->   38, "N5"->   39, "Ne"->   40, "Np"->   41, "NV"->   42, "O."->   43, "O("->   44, "O4"->   45, "O5"->   46, "O6"->   47, "O7"->   48, "O8"->   49, "O9"->   50, "Oe"->   51, "Op"->   52, "pe"->   53, "R"->    54, "R."->   55, "R0"->   56, "R2"->   57, "R3"->   58, "R4"->   59, "R5"->   60, "R6"->   61, "R8"->   62, "R9"->   63, "Rp"->   64, "S"->    65, "S."->   66, "S1"->   67, "S3"->   68, "S4"->   69, "S5"->   70, "S6"->   71, "S7"->   72, "SC"->   73, "sd"->   74, "Se"->   75, "sp"->   76, "WC"->   77, "WN"->   78, "WR"->   79];


~playStar = {
	|msg|
	var hr,ra,dec,dist,mag,spect,lum,freq;
	hr = msg[0];
	ra = msg[1];
	dec = msg[2];
	dist = msg[3];
	mag = msg[4];
	spect = msg[5];
	lum = msg[6];
	spect.postln;
	spect.class.postln;
	
	freq = ~midis[spect.asString().[0..1]].midicps;
	if (freq > 0, {
		(freq: freq, dur: 1.0).play;
	});
};

~starlistener = {
	|msg|
	~playStar.(msg[1..7]);
	msg.postln;
};
OSCFunc.newMatching(~starlistener, '/star');
