#
# File-name : perlExecuteCommand.pl
# The code is same as used and developed for CLI automation by Feroz.
# Usage: perl perlExecuteCommand.pl <option>
# Example: perl perlExecuteCommand.pl kill-hpsum
# Example: perl perlExecuteCommand.pl kill-browsers
# Example: perl perlExecuteCommand.pl start-hpsum
# Example: perl perlExecuteCommand.pl start-gatherlogs
#

$usage=<<EOS
USAGE: perl perlExecuteCommand.pl <option>
Available options:
 kill-hpsum
 kill-browsers
EOS
;

$hpsum_command_option = @ARGV[0];
chomp($hpsum_command_option);

if ($hpsum_command_option eq "") {
	die "ERROR: perlExecuteCommand: ERROR: hpsum_command_option is required.\n$usage";
}

$found = 0;

@available_options = qw(kill-hpsum
						kill-browsers
						);

if ($hpsum_command_option =~ /kill-hpsum/) {
	$rtnCode = TerminateUnixProcess("hpsum_service");  
	#return $rtnCode; 
}
elsif ($hpsum_command_option =~ /kill-browsers/) {
	$rtnCode = TerminateUnixProcess("firefox");  
	$rtnCode = TerminateUnixProcess("chrome");
	#return $rtnCode;
}
else {
	print "Executing the command : $hpsum_command_option\n";
	our $Result = `$hpsum_command_option`;
	print "RESULT: $Result\n";
	#return $Result; 
}

sub TerminateUnixProcess{
  
  our ($TaskCmd, $Pid, $bTaskCmd, $DelCmd, $Record);  
  $TaskCmd = shift;
    
  $bTaskCmd = $TaskCmd;  
  chop($TaskCmd);
  $TaskCmd = "ps -ef | grep $TaskCmd";
  our $Result = `$TaskCmd`; 
  
  if ($Result =~ /$bTaskCmd/i){    
#    print ("\r\nProcess is running...\r\n$Result\r\n");  
    
    my @Process = split("\n",$Result);  
    foreach $Record (@Process){    
      if($Record =~ /$bTaskCmd/i){
          $Pid = $1 if($Record =~ /(\d+)/i);
          last;
      }
    } 
    
    $DelCmd = "kill -9 $Pid";
    $Result = `$DelCmd`;
    $Result = `echo $?`;    
    if($Result == 0){
#      print ("\r\nSUCCESS: Terminated the process: $Pid\r\n");     
      $rtnCode = 0;
    }else{
      print ("\r\nFAILED: Terminating the process: $Pid\r\n");     
      $rtnCode = 1;
    }
  }else{
    print ("\r\nProces \"$bTaskCmd\" is NOT running\r\n");  
    $rtnCode = 0;
  }
  return $rtnCode;
}

