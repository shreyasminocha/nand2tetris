# USAGE:
# ./test.sh 
#     [PATH TO BUILT IN ASSEMBLER'S EXECUTABLE]
#     [PATH TO YOUR ASSEMBLER'S EXECUTABLE]
#     [ASM FILES TO TEST]

args=("$@")

built_in_assembler=${args[0]} # path to the builtin assembler shell script
my_assembler=${args[1]} # path to the assembler directory

NC="\033[0m" # terminate coloured output
CYAN="\033[0;36m"
GREEN="\033[0;32m"

for file in "${@:3}"; do
    extless="${file%.asm}"
    
    # delete existing assembled files so that test results are genuine
    rm -f "$extless".hack "$extless"Control.hack "$extless"Experiment.hack
    
    "$built_in_assembler" "$file" > /dev/null
    
    if [[ "$?" -eq 0 ]]; then # if file was successfully compiled
        mv "$extless".hack "$extless"Control.hack
        echo [builtin assembler] Assembled ${CYAN}"$file"${NC}
    fi
    
    "$my_assembler" "$file" "$extless"Experiment.hack > /dev/null
    
    if [[ "$?" -eq 0 ]]; then # if file was successfully compiled
        echo [my assembler]"      "Assembled ${CYAN}"$file"${NC}
    fi
    
    if [[ (-f "$extless"Control.hack) && ("$extless"Experiment.hack) ]]; then
        diff -w "$extless"Control.hack "$extless"Experiment.hack
        
        if [[ "$?" -eq 0 ]]; then
            echo " ➜" ${GREEN}"assembled files match!"${NC}
        fi
    fi
    
done
