#!/bin/bash

automation_folder="/root/automation/gui_automation"
start_log_file="$automation_folder/start.log"

user_input_hpsum_version=$1
user_input_hpsum_build=$2
user_input_spp_version=$3
user_input_spp_build=$4

echo `date` - Calling copy-extract-hpsum-spp-linux.sh with arguments $user_input_hpsum_version $user_input_hpsum_build $user_input_spp_version $user_input_spp_build >> $start_log_file

echo `date` - cronjob now: "`crontab -l`" >> $start_log_file
echo `date` - Disabling cronjob >> $start_log_file
temp_cronjob_file="/root/automation/gui_automation/tempcrontab"
crontab -l > $temp_cronjob_file
crontab -r

source_spp_path=spp.base.iso
destination_spp_path=/root/Desktop/SPP
destination_extract_spp_path=/root/Desktop/SPP/AUTOMATION
source_hpsum_path=hpsum.application
destination_hpsum_path=/root/Desktop/SPP
destination_extract_hpsum_path=/root/Desktop/SPP/

rm -rf /root/Desktop/SPP/*
umount -i -f -l /mnt/*

time_stamp=$(date | awk '{print strftime ("%d%m%Y")}')
mount_point="/mnt/HPSUM_Share"

mkdir ${mount_point}
chmod 444 ${mount_point}
mkdir /mnt/SPP
chmod 444 /mnt/SPP

mkdir ${destination_spp_path}
mkdir ${destination_extract_spp_path}
echo `date` - Mounting HPSUM_Share >> $start_log_file
mount -r -t cifs //15.154.112.188/HPSUM_Share -o username=hpdvt-user,password=Welcome123! ${mount_point}

echo `date` - Copying HPSUM.zip >> $start_log_file
##./bar -o $destination_hpsum_path/HPSUM.zip ${mount_point}/$source_hpsum_path/$user_input_hpsum_version/$user_input_hpsum_build/*.zip
##cp $mount_point/HPSUM-Builds/$1/$2/*.zip $destination_hpsum_path

cp ${mount_point}/$source_hpsum_path/$user_input_hpsum_version/$user_input_hpsum_build/*.zip $destination_hpsum_path/
mv $destination_hpsum_path/*.zip $destination_hpsum_path/HPSUM.zip

echo `date` - Extracting HPSUM.zip >> $start_log_file
unzip /root/Desktop/SPP/HPSUM.zip -d ${destination_extract_hpsum_path}

echo `date` - Copying SPP.iso >> $start_log_file
##./bar -o ${destination_spp_path}/SPP.iso ${mount_point}/$source_spp_path/$user_input_spp_version/$user_input_spp_build/*.iso
cp ${mount_point}/$source_spp_path/$user_input_spp_version/$user_input_spp_build/*.iso ${destination_spp_path}/
mv ${destination_spp_path}/*.iso ${destination_spp_path}/SPP.iso

echo `date` - Extracting SPP.iso >> $start_log_file
mkdir /mnt/SPP
mount -o loop ${destination_spp_path}/SPP.iso /mnt/SPP
cp -rf /mnt/SPP/* ${destination_extract_spp_path}

##./bar -o /mnt/SPP/*.* ${destination_extract_spp_path}

echo `date` - Updating latest HPSUM files in SPP >> $start_log_file
yes| cp -rf ${destination_extract_hpsum_path}hpsum/* ${destination_extract_spp_path}/hp/swpackages/

echo `date` - Killing existing HPSUM and firefox instance >> $start_log_file
pkill -f hpsum_service
pkill -f firefox

echo `date` - Deleting /tmp/HPSUM >> $start_log_file
rm -rf /tmp/HPSUM

echo `date` - Deleting /var/hp/log >> $start_log_file
rm -rf /var/hp/log/

echo `date` - Changing permission in swpackages >> $start_log_file
cd ${destination_extract_spp_path}/hp/swpackages/
ls -l ${destination_extract_spp_path}/hp/swpackages/
chmod +x * -R 
umount -i -f -l /mnt/*

echo `date` - Ready to run GUI-Automation >> $start_log_file
cd $automation_folder
export PATH=$PATH:/root/automation/common/bin:/root/automation/common/bin/apache-ant-1.9.0/bin
perl RUN-Linux.pl
echo `date` - GUI-Automation complete >> $start_log_file

echo `date` - Starting-CLI Automation >> $start_log_file
sh /root/automation/cli/bin/MiniMAT.sh
echo `date` - CLI-Automation complete >> $start_log_file

echo `date` - Enabling cronjob >> $start_log_file
crontab $temp_cronjob_file
rm -rf $temp_cronjob_file
echo `date` - cronjob now: "`crontab -l`" >> $start_log_file