#!/usr/bin/env sh
#
# USAGE:
# ./assembler.sh
#     [PATH TO INPUT FILE]
#     [PATH TO OUTPUT FILE]

# User's CDPATH can interfere with cd in this script
unset CDPATH
# Get the true name of this script
script="`test -L "$0" && readlink -n "$0" || echo "$0"`"
dir="$PWD"
cd "`dirname "$script"`"

if [[ ( "$#" -gt 2 ) || ( "$1" = "-h" ) || ( "$1" = "--help" ) ]]
then
	echo "Usage:"
	echo "    `basename "$0"` FILE[.asm]                 Assembles FILE.asm to FILE.hack."
	echo "    `basename "$0"` FILE[.asm] OUT[.hack]      Assembles FILE.asm to OUT.hack."
elif [[ $# -eq 1 ]]
then
	# Convert arg1 to an absolute path and run assembler with arg1.
	if [[ `echo "$1" | sed -e "s/\(.\).*/\1/"` = / ]]
	then
		arg1="$1"
	else
		arg1="${dir}/$1"
	fi

	echo Assembling "$arg1"
	java -classpath "${CLASSPATH}" Main "$arg1"
else
	if [[ `echo "$1" | sed -e "s/\(.\).*/\1/"` = / ]]
	then
		arg1="$1"
	else
		arg1="${dir}/$1"
	fi

	if [[ `echo "$2" | sed -e "s/\(.\).*/\1/"` = / ]]
	then
		arg2="$2"
	else
		arg2="${dir}/$2"
	fi

	echo Assembling "$arg1" to "$arg2"
	java -classpath "${CLASSPATH}" Main "$arg1" "$arg2"
fi
