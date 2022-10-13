#!/bin/bash
ogDir=$(pwd)
lansetDir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"
testDir="$lansetDir/tests"
interactiveTestDir="$lansetDir/tests/interactive"
outputDir="$lansetDir/out/tests"
jarFile="$lansetDir/lansetc.jar"
expectedOutDir="$lansetDir/tests/results/expectedOut"
tmpAcceptanceFile="/tmp/acceptance.sh"
interactiveInputDir="$lansetDir/tests/interactiveInput"

mkdir -p $outputDir
cd $outputDir
echo "#####Cleaning#####"
rm -f $tmpAcceptanceFile
rm -f *.class
rm -f *.out
rm -f *.err
echo

#Build
#TODO build in order
echo "#####Building#####"
for testFile in {$testDir,$interactiveTestDir}/*.lans; do
	java -jar $jarFile $testFile
	echo
done

#Run
#Prerequisite: test files name and program name must match
#TODO run only if the build has succeded
echo "#####Running normal tests#####"
for testFile in $testDir/*.lans; do
	programName=$(basename $testFile | sed 's/.lans//g')
	echo Running $programName
	java $programName >> $programName.out 2>> $programName.err

	if [ -s "$programName.err" ] 
	then
		echo "$programName has printed some errors:"
		cat $programName.err
		echo "-----end of errors-----"
	fi

	expectedOutFile="$expectedOutDir/$programName.out"
	realOutFile="$programName.out"
	if [ ! -f $expectedOutFile ]
	then
		echo Creating new expected out for $programName
		cp $programName.out $expectedOutFile
	else
		if cmp -s "$expectedOutFile" "$realOutFile"
		then
			echo Ok!
		else
			echo Differences found!
			diff "$expectedOutFile" "$realOutFile"
			echo "-----end of differences------"
			echo "cp $outputDir/$realOutFile $expectedOutFile" >> $tmpAcceptanceFile
		fi
	fi
	echo
done

echo "#####Running Interactive tests#####"
for testFile in $interactiveTestDir/*.lans; do
	programName=$(basename $testFile | sed 's/.lans//g')
	echo Running $programName

	interactiveInputFile="$interactiveInputDir/$programName.in"
	if [ -f $interactiveInputFile ]
	then
		java $programName >> $programName.out 2>> $programName.err < $interactiveInputFile

	if [ -s "$programName.err" ] 
	then
		echo "$programName has printed some errors:"
		cat $programName.err
		echo "-----end of errors-----"
	fi

	expectedOutFile="$expectedOutDir/$programName.out"
	realOutFile="$programName.out"
	if [ ! -f $expectedOutFile ]
	then
		echo Creating new expected out for $programName
		cp $programName.out $expectedOutFile
	else
		if cmp -s "$expectedOutFile" "$realOutFile"
		then
			echo Ok!
		else
			echo Differences found!
			diff "$expectedOutFile" "$realOutFile"
			echo "-----end of differences------"
			echo "cp $outputDir/$realOutFile $expectedOutFile" >> $tmpAcceptanceFile
		fi
	fi
	else
		echo Warning! The interactive test $programName does NOT have an input defined!
	fi

	echo
done

if [ -f $tmpAcceptanceFile ]
then
	chmod 755 $tmpAcceptanceFile
	echo "To accept all differences as good run $tmpAcceptanceFile"
fi

cd $ogDir
