#!/usr/bin/perl
use strict;
my @hands = (
	[map { 0 } (0..127)],#useless hand. Ha!
	[map { 0 } (0..127)],
	[map { 0 } (0..127)]
);
sub midi2time {
	my ($midi) = @_;
	# I don't get it
	return $midi / 750;
}
while(<>) {
	chomp;
	s/^\s*//;
	s/\s*$//;
	my ($time,$hand,$midi,$vel) = split(/\s*,\s*/,$_);
	if ($hands[$hand][$midi] > 0) {
		print join(",",midi2time($hands[$hand][$midi]),midi2time($time-$hands[$hand][$midi]),$hand,$midi,$/);
		$hands[$hand][$midi] = 0;
	} else {
		$hands[$hand][$midi] = $time;
	}
}
