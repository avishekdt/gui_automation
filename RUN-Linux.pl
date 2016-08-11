# 
# File-name: RUN-Linux.pl
# Usage: perl RUN-Linux.pl
# Use this file to start automation execution on Linux server.
#

$ts = GenerateTimeStamp();
#print "Timestamp = $ts\n";

# ******* PROVIDE THE VALUES ********************************************
$input_copyLogsToRemoteShare=0;
$input_remoteShareLocation="\\\\15.154.112.188\\HPSUM_Logs";
$input_remoteShareLocationFolder="Automation-Logs\\GUI-Automation";
$input_remoteShareUser="ASIAPACIFIC\\hpdvt-user";
$input_remoteSharePwd="Welcome123!";
$input_fromEmailAddrConsMail="automation-hpsum\@hp.com";
$input_toEmailAddrConsMail="ajay.ksingh\@hp.com";
# ***********************************************************************

$remoteShareLogsPath=$input_remoteShareLocation . "\\" . $input_remoteShareLocationFolder;
$localLogDir="logs";

$ENV{'input_copyLogsToRemoteShare'} = $input_copyLogsToRemoteShare;
$ENV{'remoteShareLogsPath'} = $remoteShareLogsPath;
$ENV{'input_remoteShareUser'} = $input_remoteShareUser;
$ENV{'input_remoteSharePwd'} = $input_remoteSharePwd;
$ENV{'input_fromEmailAddrConsMail'} = $input_fromEmailAddrConsMail;
$ENV{'input_toEmailAddrConsMail'} = $input_toEmailAddrConsMail;
$ENV{'localLogDir'} = $localLogDir;

$path = `echo \$PATH`;
chomp($path);
print "PATH=$path\n";

$path_add_1 = "/root/automation/common/bin";
$path_add_2 = "/root/automation/common/bin/apache-ant-1.9.0/bin";
$ENV{'TIME_STAMP'} = $ts;
#export PATH=$PATH:/root/automation/common/bin:/root/automation/common/bin/apache-ant-1.9.0/bin;

if (!($path_add_1 =~ /$path/)) {
		if (!($path_add_2 =~ /$path/)) {
			#`export PATH=\$PATH:/root/automation/common/bin:/root/automation/common/bin/apache-ant-1.9.0/bin`;
			print "\nPlease execute below command on this prompt and rerun this script:\n";
			print "export PATH=\$PATH:/root/automation/common/bin:/root/automation/common/bin/apache-ant-1.9.0/bin\n";
		}
		else {
			#`export PATH=\$PATH:/root/automation/common/bin/apache-ant-1.9.0/bin`;
			print "\nPlease execute below command on this prompt and rerun this script:\n";
			print "export PATH=\$PATH:/root/automation/common/bin/apache-ant-1.9.0/bin\n";
		}
		#$path = `echo \$PATH`;
		#chomp($path);
		#print "PATH Now = $path\n";
}
else {
	print "The PATH is already up-to-date with the ANT variables.\n";
}

$pwd = `pwd`;
chomp($pwd);
$log_file = $pwd . "/logs/" . $ts . "_Automation-Run.log";
print "\nLOG File: $log_file\n";
print "Started the execution. Please wait...\n";
`ant sendMail > $log_file`;
print "Execution complete. See the log file.\n";
print "$log_file\n\n";


sub GenerateTimeStamp{
  our ($sec, $min, $hour, $day, $month, $year) = (localtime)[0..5];
  $month++;
  $year += 1900;
  $month= "0".$month if($month =~ m/^\d$/);
  $day  = "0".$day  if($day =~ m/^\d$/);  
  $hour = "0".$hour if($hour =~ m/^\d$/);
  $min  = "0".$min  if($min =~ m/^\d$/);
  $sec  = "0".$sec  if($sec =~ m/^\d$/);  
  
  return ("$year$month$day"."-"."$hour$min$sec");
}

system("umount -i -f -l /mnt/\*");
system("mkdir /mnt/HPSUM_Logs-$ts");
system("mount -t cifs //15.154.112.188/HPSUM_Logs -o username=hpdvt-user,password='Welcome123\!' /mnt/HPSUM_Logs-$ts");
system("mkdir /mnt/HPSUM_Logs-$ts/Automation-Logs/GUI-Automation/$ts");system("cp -rf /root/automation/gui_automation/logs /mnt/HPSUM_Logs-$ts/Automation-Logs/GUI-Automation/$ts");
