use strict;
use warnings;

my $file = 'messages.htm';
open my $info, $file or die "could not open";

while (my $row = <$info>) {
	$row =~ s/\&\#039\;/\'/g;
	$row =~ s/\&quot\;/\"/g;
	$row =~ s/\&lt\;/\</g;
	$row =~ s/\&gt\;/\</g;
	while ($row =~/(?<=<p>)(.*?)(?=<\/p>)/g) {
		my $statement = "$1\n";
		if ($statement =~ /^[a-zA-Z]/) {
			print $statement;
		}
	}
}

close $info;