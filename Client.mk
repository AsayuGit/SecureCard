# opencard.core.*
export CLASSES = ;$(OCF_HOME)\lib\base-core.jar \

# opencard.opt.util
export CLASSES+=;$(OCF_HOME)\lib\base-opt.jar

# bouncy castle (crypto provider)
export CLASSES+=;$(MISC)\bcprov-jdk15on-150.jar

export RUN_CLASSES = ;$(OUT)/ \
	;$(JC22_HOME)\lib\apduio.jar \
	;$(CLASSES) \
	;$(MISC)\$(PCSC_WRAPPER)\lib\$(PCSC_WRAPPER).jar \
	;$(MISC)\$(APDUIO_TERM)\lib\$(APDUIO_TERM).jar

export DEP_DIR = dependencies

build: $(OBJ)
	@echo Compiling $(CLIENT).java ...
	@$(JCC) -sourcepath "$(SRC_DIR)" -classpath "$(CLASSES)" -g -d "$(OUT)" $(SRC_DIR)/$(PKGCLIENT)/$(CLIENT).java
	@echo $(CLIENT).java compiled: OK

jar: build
	@if not exist "$(DEP_DIR)" mkdir "$(DEP_DIR)"

	@echo Extracting dependencies ...
	@powershell cd $(DEP_DIR); ../$(JAR) xf "../$(OCF_HOME)\lib\base-core.jar" opencard \
	; ../$(JAR) xf "../$(OCF_HOME)\lib\base-opt.jar" opencard \
	; ../$(JAR) xf "../$(JC22_HOME)\lib\apduio.jar" com \
	; ../$(JAR) xf "../$(MISC)\bcprov-jdk15on-150.jar" com \
	; ../$(JAR) xf "../$(MISC)\$(PCSC_WRAPPER)\lib\$(PCSC_WRAPPER).jar" com \
	; ../$(JAR) xf "../$(MISC)\$(APDUIO_TERM)\lib\$(APDUIO_TERM).jar" com \

	@echo Bundling sources ...
	@$(JAR) cfe "$(CLIENT).jar" "$(PKGCLIENT)/$(CLIENT)" -C . src misc Makefile Client.mk Applet.mk opencard.properties ocfpcsc1.dll

	@echo Bunling classes ...
	@$(JAR) ufe "$(CLIENT).jar" "$(PKGCLIENT)/$(CLIENT)" -C "$(OUT)" .

	@echo Bundling dependencies ...
	@$(JAR) ufe "$(CLIENT).jar" "$(PKGCLIENT)/$(CLIENT)" -C "$(DEP_DIR)" .

run:
	@$(JVM) -Djava.library.path=. -classpath "$(RUN_CLASSES)" $(PKGCLIENT).$(CLIENT)

runJar:
	@$(JVM) -jar $(CLIENT).jar
