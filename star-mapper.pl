#!/usr/bin/perl
# HA~!
# use this to make star-map.tsv
@startypes = `awk -F, '{print \$16}' public/HYG/hygdata_v3.csv |lc | sort -u`;
chomp(@startypes);
@datfiles = <stars/*.dat>;
@starfiles = map { $a=$_;$a=~s/stars\///;$a } @datfiles;
@starfiles = map { $a=$_;$a=~s/\.dat$//;$a } @starfiles;
@starfiles = map { lc($_) } @starfiles;
my %exact = ();
my $index = 0;
foreach my $dat (@starfiles) {
	$exact{$dat} = $datfiles[$index];
	$index++;
}
sub sim {
	my ($x,$y) = @_;
	my $diff = length($x) - length($y);
	my $match = ($diff==0)?0.5:-1*abs($diff/10);
	for my $i (0..length($x)) {
		if (substr($x,$i,1) eq substr($y,$i,1)) {
			$match++;
		} else {
			return $match;
		}
	}
	return $match;
}
sub find_match {
	my ($star) = @_;
	$star =~ s/[)( ]//g;
	if (exists $exact{$star}) {
		return $exact{$star};
	}
	my $hit;
	my $m = 0;
	foreach my $file (@starfiles) {
		my $v = sim($star,$file);
		if ($v > $m) {
			$hit = $file;
			$m = $v;
		}
	}
	return $hit;
}

for my $star (@startypes) {
	my $match = find_match($star);
	print join("\t",$star,$match,$exact{$match},$/);
}
