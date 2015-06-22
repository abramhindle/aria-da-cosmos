s.options.numBuffers = 16000;
s.options.memSize = 655360;
s.boot;
s.freqscope;
s.plotTree;
s.scope;

SynthDef(\hydro3, {
	|out=0,amp=1.0,freq=440|
	var nsize,n = (2..10);
	nsize = n.size;
	Out.ar(0,
		amp * 
		(
			n.collect {arg i; 
				SinOsc.ar( (1.0 - (1/(i*i))) * freq ) +
				SinOsc.ar( ((1/4) - (1/((i+1)*(i+1)))) * freq)
			}).sum / (2 * nsize)
	)
}).add;



// angstroms 10X than nanometers
~minang = 3500;
~maxang = 7500;

~rows = TabFileReader.read("star-map.tsv");
~starmap = Dictionary.new();
~rows.do {|v| 
	~starmap[v[0]] = v[1];
};

~dacapo = CSVFileReader.read("midi/dacapo.csv");
~dacapo = ~dacapo.collect {|v| v[1] = v[1].asFloat; v[2] =
	v[2].asFloat; v[3] = v[3].asFloat; };

~dacapoi = 0;


~emissioninst = {
	arg filename;
	var rows,fmin,fmax,out;
	rows = CSVFileReader.read(filename);//"emission/a0i.dat"); 
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
~estars = ["a0i", "a0iii", "a0iv", "a0v", "a2i", "a2v", "a3iii", "a3v", "a47iv", "a5iii", "a5v", "a7iii", "a7v", "b0i", "b0v", "b12iii", "b1i", "b1v", "b2ii", "b2iv", "b3i", "b3iii", "b3v", "b57v", "b5i", "b5ii", "b5iii", "b6iv", "b8i", "b8v", "b9iii", "b9v", "f02iv", "f0i", "f0ii", "f0iii", "f0v", "f2ii", "f2iii", "f2v", "f5i", "f5iii", "f5iv", "f5v", "f6v", "f8i", "f8iv", "f8v", "g0i", "g0iii", "g0iv", "g0v", "g2i", "g2iv", "g2v", "g5i", "g5ii", "g5iii", "g5iv", "g5v", "g8i", "g8iii", "g8iv", "g8v", "k01ii", "k0iii", "k0iv", "k0v", "k1iii", "k1iv", "k2i", "k2iii", "k2v", "k34ii", "k3i", "k3iii", "k3iv", "k3v", "k4i", "k4iii", "k4v", "k5iii", "k5v", "k7v", "m0iii", "m0v", "m10iii", "m1iii", "m1v", "m2i", "m2iii", "m2p5v", "m2v", "m3ii", "m3iii", "m3v", "m4iii", "m4v", "m5iii", "m5v", "m6iii", "m6v", "m7iii", "m8iii", "m9iii", "o5v", "o8iii", "o9v", "rf6v", "rf8v", "rg0v", "rg5iii", "rg5v", "rk0iii", "rk0v", "rk1iii", "rk2iii", "rk3iii", "rk4iii", "rk5iii", "uka0i", "uka0iii", "uka0iv", "uka0v", "uka2i", "uka2v", "uka3iii", "uka3v", "uka47iv", "uka5iii", "uka5v", "uka7iii", "uka7v", "ukb0i", "ukb0v", "ukb12iii", "ukb1i", "ukb1v", "ukb2ii", "ukb2iv", "ukb3i", "ukb3iii", "ukb3v", "ukb57v", "ukb5i", "ukb5ii", "ukb5iii", "ukb6iv", "ukb8i", "ukb8v", "ukb9iii", "ukb9v", "ukf02iv", "ukf0i", "ukf0ii", "ukf0iii", "ukf0v", "ukf2ii", "ukf2iii", "ukf2v", "ukf5i", "ukf5iii", "ukf5iv", "ukf5v", "ukf6v", "ukf8i", "ukf8iv", "ukf8v", "ukg0i", "ukg0iii", "ukg0iv", "ukg0v", "ukg2i", "ukg2iv", "ukg2v", "ukg5i", "ukg5ii", "ukg5iii", "ukg5iv", "ukg5v", "ukg8i", "ukg8iii", "ukg8iv", "ukg8v", "ukk01ii", "ukk0iii", "ukk0iv", "ukk0v", "ukk1iii", "ukk1iv", "ukk2i", "ukk2iii", "ukk2v", "ukk34ii", "ukk3i", "ukk3iii", "ukk3iv", "ukk3v", "ukk4i", "ukk4iii", "ukk4v", "ukk5iii", "ukk5v", "ukk7v", "ukm0iii", "ukm0v", "ukm10iii", "ukm1iii", "ukm1v", "ukm2i", "ukm2iii", "ukm2p5v", "ukm2v", "ukm3ii", "ukm3iii", "ukm3v", "ukm4iii", "ukm4v", "ukm5iii", "ukm5v", "ukm6iii", "ukm6v", "ukm7iii", "ukm8iii", "ukm9iii", "uko5v", "uko8iii", "uko9v", "ukrf6v", "ukrf8v", "ukrg0v", "ukrg5iii", "ukrg5v", "ukrk0iii", "ukrk0v", "ukrk1iii", "ukrk2iii", "ukrk3iii", "ukrk4iii", "ukrk5iii", "ukwf5v", "ukwf8v", "ukwg0v", "ukwg5iii", "ukwg5v", "ukwg8iii", "ukwk0iii", "ukwk1iii", "ukwk2iii", "ukwk3iii", "ukwk4iii", "wf5v", "wf8v", "wg0v", "wg5iii", "wg5v", "wg8iii", "wk0iii", "wk1iii", "wk2iii", "wk3iii", "wk4iii"];
~estar = Dictionary.new();
~estars.do {|elm| ~estar[elm] = ~emissioninst.("emission/" ++ elm ++ ".dat")};
~estar["a0i"];

~lookupestar = {
	|star|
	star = star.asString.toLower;
	~estar.atFail(star,{~estar.at(~starmap[star])})
};
~lookupastar = {
	|star|
	star = star.asString.toLower;
	~astar.atFail(star,{~astar.at(~starmap[star])})
};

//~lookupstar.("a0i")
//~lookupstar.("a:")

~absorptioninst = {
	arg filename;
	var rows,fmin,fmax,out;
	rows = CSVFileReader.read(filename); 
	rows.removeAt(0);
	rows = rows.collect {|v| 
		var o = [nil,v[1].asFloat,v[3].asFloat];
		o 
	};
	out = Dictionary.new();
	out["rows"] = rows;
	out["max"] = rows[rows.maxIndex{|v|v[2]}].[2];
	out["min"] = rows[rows.minIndex{|v|v[2]}].[2];
	out["fmax"] = rows[rows.maxIndex{|v|v[1]}].[1];
	out["fmin"] = rows[rows.minIndex{|v|v[1]}].[1];
	out
};

~astar = Dictionary.new();
~estars.do {|elm| ~astar[elm] = ~absorptioninst.("absorption/" ++ elm ++ ".dat")};
~astar["a0i"];

// sound conversion
~onehz = 343.36; // meters
~lowlen = ~onehz/20;
~highlen = ~onehz/20000;
~soundrange = ~lowlen - ~highlen;
~angstrom = 1e-10;
~angrange = ~maxang - ~minang;
~angtopitch = {
	arg ang;
	var i = (ang-~minang)/~angrange;
	~onehz/(i*~soundrange+~highlen)
};


~mkelement = {
	arg element, bfreq=1.0, n=30;
	var hv,hvp,hva,emin,emax;
	emin = (element["min"]);
	emax = (element["max"]);
	hv = element["rows"][{element["rows"].size.rand}!n];
	hvp = hv.collect {|v| ~angtopitch.(v[1]) * bfreq };
	hva = hv.collect {|v| (v[2])/(emax) };	
	hvp.postln;
	hva.postln;
	{
		|freq=1.0,amp=0.0,attack=20,decay=20|
		Normalizer.ar(
			Mix.ar( SinOsc.ar(freq * hvp, mul: hva) ) / n,
		)!2 * EnvGen.kr(
			Env.new([0,0.5,0],[attack,decay]),
			doneAction: 2
		)!2 * amp
	}
};





~playestar = {|elm,amp=0.0,freq=440.0,attack=20.0,decay=20.0,n=100| ~mkelement.(~lookupestar.(elm),n:n).play(s,args:[\amp,amp,\freq,freq,\attack,attack,\decay,decay]) };
~playastar = {|elm,amp=0.0,freq=440.0,attack=20.0,decay=20.0,n=100| ~mkelement.(~lookupastar.(elm),n:n).play(s,args:[\amp,amp,\freq,freq,\attack,attack,\decay,decay]) };

/*
~playestar.("a0i",freq: 44,amp: 0.1);
~playastar.("a0i",freq: 10,amp: 0.1);
~playestar.("a:",freq: 44,amp: 0.1);
~playastar.("a:",freq: 10,amp: 0.1);
*/



~playStar = {
	|msg|
	var hr,ra,dec,dist,mag,spect,lum,freq,dacapo,mdur;
	hr = msg[0];
	ra = msg[1];
	dec = msg[2];
	dist = msg[3];
	mag = msg[4];
	spect = msg[5];
	lum = msg[6];
	spect.postln;
	spect.class.postln;
	dacapo = ~dacapo[~dacapoi];
	mdur = 2*dacapo[1]*0.8;
	if(dacapo[2]==2,{
		~playestar.(spect,freq: dacapo[3].midicps/5.0,amp:
			0.1,attack:2*dacapo[1]*0.2,decay:min(mdur,20));
	}, {
		~playastar.(spect,freq: dacapo[3].midicps/5.0,amp:
			0.1,attack:2*dacapo[1]*0.2,decay:min(mdur,20));
	});
	(freq: dacapo[3].midicps, amp:0.1,  dur: min(dacapo[1],20)).play;
	~dacapoi = ~dacapoi + 1;
};

~starlistener = {
	|msg|
	msg.postln;
	~playStar.(msg[1..7]);
};
OSCFunc.newMatching(~starlistener, '/star');
~hydro3 = Synth(\hydro3);
~hydro3.set(\amp,0.3)
~end = {
	var amp = 0.3;
	~dacapoi = 10000000000000;
	Routine({
		100.do {|x|
			amp = amp / 1.1;
			~hydro3.set(\amp,amp);
			0.1.wait;
		}
	}).play();
};
OSCFunc.newMatching(~endlistener, '/end');
//~end.();