# USAGE:
# ./test.sh [PATH TO BUILT IN ASSEMBLER] [PATH TO YOUR ASSEMBLER'S DIRECTORY] [ASM FILES TO TEST]

args=("$@")

builtInAssembler=${args[0]} # path to the builtin assembler shell script
myAssembler=${args[1]} # path to the assembler directory

NC="\033[0m" # terminate coloured output
CYAN="\033[0;36m"
GREEN="\033[0;32m"

for file in "${@:3}"; do
    extless="${file%.asm}"
    
    "$builtInAssembler" "$file" > /dev/null
    if [[ "$?" ]]; then
        echo [builtin assembler] ${CYAN}"$file"${NC}
    fi
    mv "$extless".hack "$extless"Control.hack
    
    
    java -cp "$myAssembler" Main "$file"
    if [[ "$?" ]]; then
        echo [your assembler] ${CYAN}"$file"${NC}
    fi
    mv "$extless".hack "$extless"Experiment.hack
    
    diff -w "$extless"Control.hack "$extless"Experiment.hack
    if [[ "$?" ]]; then
        echo " ➜" ${GREEN}"files matched!"${NC}
    fi
done
