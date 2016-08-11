$usage=<<EOS
USAGE: perl start_hpsum_linux.pl hpsum_sh_file_path
USAGE: perl perlStartScript.pl <hpsum_sh_file_path> <filename>
Examples: 
	perl perlStartScript.pl /root/Desktop/SPP/GUI-AUTOMATION/hp/swpackages hpsum
	perl perlStartScript.pl /root/Desktop/SPP/GUI-AUTOMATION/hp/swpackages gatherlogs.sh
EOS
;

#
# Verify that an argument has been provided.
#
$hpsum_sh_file_path = @ARGV[0];
$hpsum_sh_file_name = @ARGV[1];

if ($hpsum_sh_file_path eq "") {
	die "ERROR:: hpsum_sh_file_path is required.\n$usage";
}
if ($hpsum_sh_file_name eq "") {
	die "ERROR:: hpsum_sh_file_name is required.\n$usage";
}

print "\nStarting script $hpsum_sh_file_name from $hpsum_sh_file_path\n";

`cd $hpsum_sh_file_path;chmod +x hpsum; chmod +x x*/*; chmod +x $hpsum_sh_file_name; ./$hpsum_sh_file_name`;
`pwd`;

print "\nDone\n";
