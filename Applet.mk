
build:
	@echo Compilating $(APPLET).java ...
	@$(JCC) -source 1.2 -target 1.2 -sourcepath "$(SRC_DIR)" -classpath $(JC22_API) -g -d "$(OUT)" $(SRC_DIR)/$(PKGAPPLET)/$(APPLET).java
	@echo $(APPLET).java compiled: OK

	@echo Converting...
	@$(JVM) -classpath $(JC22_HOME)/lib/converter.jar;$(JC22_HOME)/lib/offcardverifier.jar com.sun.javacard.converter.Converter -nobanner -classdir $(OUT)/ -exportpath $(JC_EXP) -d $(OUT)/ -out EXP JCA CAP -applet $(AIDAPPLET) $(PKGAPPLET).$(APPLET) $(PKGAPPLET) $(AIDPACKAGE) $(PKGVERSION)
	@powershell copy "$(OUT)/$(PKGAPPLET)/javacard/$(PKGAPPLET).cap" "$(OUT)"

	@echo Scripting...
	@$(JVM) -classpath $(JC22_HOME)/lib/scriptgen.jar com.sun.javacard.scriptgen.Main -nobanner -o $(OUT)/$(APPLET).scr $(OUT)/$(PKGAPPLET)/javacard/$(PKGAPPLET).cap
	@if errorlevel 1 goto error
	@echo $(APPLET).scr created: OK

	@echo Completing script...

	@powershell cat "$(MISC)/Header.scr", "$(OUT)/$(APPLET).scr", "$(MISC)/Install.scr", "$(MISC)/Footer.scr" > "$(OUT)/$(SIMUSCRIPT).scr"
	@powershell rm $(OUT)/$(APPLET).scr
	@echo $(SIMUSCRIPT).scr created: OK

burn: deleteApplet installApplet
	@echo Applet burn successful !

deleteApplet:
	@echo Deleting applet ...

	@echo mode_201 > "$(OUT)/config.txt"
	@echo gemXpressoPro >> "$(OUT)/config.txt"
	@echo enable_trace >> "$(OUT)/config.txt"
	@echo establish_context >> "$(OUT)/config.txt"
	@echo card_connect >> "$(OUT)/config.txt"
	@echo select -AID $(CARDSECURITYDOMAINGXPPROR32E64PK) >> "$(OUT)/config.txt"
	@echo open_sc -security $(SECURITYGXPPRO) -keyind 0 -keyver $(KEYVERSIONGXPPROR32E64PK) -key $(CARDKEYGXPPRO) >> "$(OUT)/config.txt"
	@echo delete -AID $(APPLETAID) >> "$(OUT)/config.txt"
	@echo delete -AID $(PACKAGEAID) >> "$(OUT)/config.txt"
	@echo card_disconnect >> "$(OUT)/config.txt"
	@echo release_context >> "$(OUT)/config.txt"

	@"$(GPSHELL)/gpshell.exe" < "$(OUT)/config.txt"
	@powershell rm "$(OUT)/config.txt"
	@echo Applet deleted

installApplet:
	@echo Installing applet ...

	@echo mode_201 > "$(OUT)/config.txt"
	@echo gemXpressoPro >> "$(OUT)/config.txt"
	@echo enable_trace >> "$(OUT)/config.txt"
	@echo establish_context >> "$(OUT)/config.txt"
	@echo card_connect >> "$(OUT)/config.txt"
	@echo select -AID $(CARDSECURITYDOMAINGXPPROR32E64PK) >> "$(OUT)/config.txt"
	@echo open_sc -security $(SECURITYGXPPRO) -keyind 0 -keyver $(KEYVERSIONGXPPROR32E64PK) -key $(CARDKEYGXPPRO) >> "$(OUT)/config.txt"
	@echo install -file $(OUT)/$(PKGAPPLET)/javacard/$(PKGAPPLET).cap -sdAID $(CARDSECURITYDOMAINGXPPROR32E64PK) -nvCodeLimit 4000 >> "$(OUT)/config.txt"
	@echo card_disconnect >> "$(OUT)/config.txt"
	@echo release_context >> "$(OUT)/config.txt"

	@"$(GPSHELL)/gpshell.exe" < "$(OUT)/config.txt"
	@powershell rm "$(OUT)/config.txt"
	
	@echo Applet installed !