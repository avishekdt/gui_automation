#!/bin/bash
output=$(ps -e|grep copy-extract)
if [[ $output != *"copy-extract"* ]] 
then

	automation_folder="/root/automation/gui_automation"
	start_log_file="$automation_folder/start.log"
	echo `date` ----- Scheduler Started ----- >> $start_log_file

	{
		echo "Unmounting /mnt/* dirs and creating /mnt/Share dir"
		umount -i -f -l /mnt/*
		mkdir /mnt/Share
	} 2> /dev/null
	
	chmod 444 /mnt/Share
	mount -t cifs //15.154.112.188/automation/gui -o username=hpdvt-user,password=Welcome123! /mnt/Share/
	mainFile="/mnt/Share/main_hpsum_lin.txt"
	snap2="/mnt/Share/Gen9Snap2.txt"
	snap3="/mnt/Share/Gen9Snap3.txt"

	#reading the file from hpsum_build file
	n=1
	while read -r LINE; do
		if test $n -eq 1 
		then
		   var01=$LINE
		fi
		if test $n -eq 2
		then
		   var02=$LINE
		fi
		n=`expr $n + 1`
	done < <(tr -d '\r' < $mainFile)
	##"tr -d /r" - trims the new line in variable

	if [ "$var01" = "" ]
	then
		echo `date` - No new HPSUM build found >> $start_log_file
	else
		if [ $var01 = "7.2.0" ]
		then
			snapFile=$snap2
		elif [ $var01 = "7.3.0" ]
		then
			snapFile=$snap3
		fi
		echo `date` - New HPSUM version:$var01 and build:$var02 found, starting automation >> $start_log_file
		var03=$(tail -2 $snapFile | head -1)
		var04=$(tail -1 $snapFile | head -1)
		var03=$(echo $var03 | sed -e 's/\r//g')
		var04=$(echo $var04 | sed -e 's/\r//g')
		#Emptying the mailFile
		cat /dev/null > $mainFile
	
		#Sending email
		echo `date` - Sending email with $var01 $var02 $var03 $var04 >> $start_log_file
		sh mail.sh $var01 $var02 $var03 $var04
	
		cd $automation_folder
		chmod +x copy_lin/copy-extract-hpsum-spp-linux.sh
		./copy_lin/copy-extract-hpsum-spp-linux.sh $var01 $var02 $var03 $var04
	fi
	echo `date` - Good bye >> $start_log_file
	umount -i -f -l /mnt/*
else
	echo ""
fi
