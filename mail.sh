subject="Linux: MAT Automation Started - HPSUM version:$1 build:$2 and SPP version:$3 build:$4<EOM>"
from="automation-hpsum@hpe.com"
recipients="automation-hpsum@hpe.com"
mail="subject:$subject\nfrom:$from\n"
echo -e $mail | /usr/sbin/sendmail -F "Automation" "$recipients"
