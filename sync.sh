#!/bin/bash
git pull || exit 1

#####   vorher  -->  play build-module  <-- aufrufen

PROJEKT=`pwd | rev | cut -d "/" -f 1 | rev | sed -s s"|-||"`
echo "$PROJEKT"
DEP=conf/dependencies.yml

if [ "x$1" = "x--go" ]; then
	echo "Kopiere alle Dateien aus dist/ nach playmodules.teuto.net"
else 
	while true; do
		read -p  "Modul neu bauen? [J]n] " yn
		if [ "x$yn" = "x" ]; then
			yn="j"
		fi
		case $yn in
			[Nn]* ) echo "Modul nicht neu gebaut"
					break
					;;
			[JjYy]) 
					echo "Baue Modul neu"
					DEPBAK=${DEP}.bak
					
					MAJOR=`head -n 1 ${DEP} | cut  -d ' ' -f 5 | cut -d '.' -f 1`
					SUB=`head -n 1 ${DEP} | cut  -d ' ' -f 5 | cut -d '.' -f 2`
					MINOR=`head -n 1 ${DEP} | cut  -d ' ' -f 5 | cut -d '.' -f 3`
					
					VERSION="$MAJOR.$SUB.$MINOR"	
					git fetch
					LAST_USED_VERSION="$(git tag | sed -s 's/\(.*-\)\?\([0-9]\+\.[0-9]\+\.[0-9]\+\).*/\2/' | sort -t. -k 1,1nr -k 2,2nr -k 3,3nr | head -n 1)"
					
					if [ "$VERSION" != "$LAST_USED_VERSION" ]; then
						echo "last used version number is $LAST_USED_VERSION. The version number in dependencies.yml is $VERSION"
						while true; do
							read -p "should the version number be replaced with the last used before sync?  [J|n] " yn
							if [ "x$yn" = "x" ]; then
								yn="j"
							fi
							case $yn in
								[nN])
									break;
								;;
								[JjYy]) 
									sed -i -s s"|$PROJEKT $VERSION|$PROJEKT $LAST_USED_VERSION|" ${DEP}
									break;
								;;
							esac
						done
					fi
					
					# versionsnummer neu ermitteln, da sie sich eben geändert haben könnte
					MAJOR=`head -n 1 ${DEP} | cut  -d ' ' -f 5 | cut -d '.' -f 1`
					SUB=`head -n 1 ${DEP} | cut  -d ' ' -f 5 | cut -d '.' -f 2`
					MINOR=`head -n 1 ${DEP} | cut  -d ' ' -f 5 | cut -d '.' -f 3`
					declare i MINOR=$((MINOR+1))
					VERSION="$MAJOR.$SUB.$MINOR"
					sed  -e "s/$PROJEKT .*/$PROJEKT $MAJOR.$SUB.$MINOR/" $DEP > $DEPBAK
					mv $DEPBAK $DEP
					echo "exec 'play build-module'"
					play build-module 
					
					echo "markiere aktuellen Zustand zur Wiederherstellung von gelöschten Modulversionen"
					git add ${DEP}
                    git commit -m "$MAJOR.$SUB.$MINOR"
                    git tag -a $MAJOR.$SUB.$MINOR -m "$MAJOR.$SUB.$MINOR"
                    git push
                    git push --tags
					break
					;;
			*)      echo "[$yn]"    
		esac
	done
	while true; do
		read -p  "Sollen alle Dateien aus dist/ nach playmodules.teuto.net kopiert werden? [J]n] " yn
		if [ "x$yn" = "x" ]; then
			yn="j"
		fi
		case $yn in 
			[Nn]* ) echo "Dann eben nicht."
					exit 1
					;;
			[JjYy]) 
					echo "OK"
					break
					;;
			*)      echo "[$yn]"    
		esac
	done
fi
		
scp dist/* root@devel.teuto.net:/home/playmodules.teuto.net/docs/mod/

