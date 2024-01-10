# General ENV
export PROJECT=cartapus

export DIR=.
export OUT=$(DIR)/out/$(PROJECT)
export SRC_DIR = $(DIR)/src/$(PROJECT)
export MISC=$(DIR)/misc

export CAPTRANSF=$(MISC)/captransf-1.5
export GPSHELL=$(MISC)/gpshell-1.4.2
export JC21_HOME=$(MISC)/java_card_kit-2_1_2
export JC22_HOME=$(MISC)/java_card_kit-2_2_1
export JAVA_HOME=$(MISC)/jdk1.6
export OCF_HOME=$(MISC)/OCF1.2
export PCSC_WRAPPER=pcsc-wrapper-2.0
export APDUIO_TERM=apduio-terminal-0.1

export JC21_API=$(JC21_HOME)/lib/api21.jar
export JC22_API=$(JC22_HOME)/lib/api.jar
export JC21_EXP=$(JC21_HOME)/api21_export_files
export JC22_EXP=$(JC22_HOME)/api_export_files

export JC_EXP=$(JC21_EXP)

export JCC = "$(JAVA_HOME)/bin/javac.exe"
export JVM = "$(JAVA_HOME)/bin/java.exe"
export JAR = "$(JAVA_HOME)/bin/jar.exe"

export PKGCLIENT=client
export CLIENT=TheClient
export PKGAPPLET=applet
export APPLET=TheApplet

export AIDPACKAGE=0xA0:0x00:0x00:0x00:0x62:0x03:0x01:0x0C:0x06
export PACKAGEAID=A00000006203010C06
export AIDAPPLET=0xA0:0x00:0x00:0x00:0x62:0x03:0x01:0x0C:0x06:0x01
export APPLETAID=A00000006203010C0601

export CARDSECURITYDOMAIN=A000000003000000
export CARDSECURITYDOMAIN2=A0000000030000
export CARDSECURITYDOMAINGXPPRO=A000000018434D
export CARDSECURITYDOMAINGXPPROR32E64PK=A000000018434D00
export CARDKEY=404142434445464748494A4B4C4D4E4F
export CARDKEYGXPPRO=47454d5850524553534f53414d504c45
export SECURITYGXPPRO=0
export KEYVERSIONGXPPRO=13
export KEYVERSIONGXPPROR32E64PK=00

export PKGVERSION=1.0
export SIMUSCRIPT=script

all: applet client jar

applet: $(OUT)
	@$(MAKE) -f Applet.mk --no-print-directory build

client: $(OUT)
	@$(MAKE) -f Client.mk --no-print-directory build

$(OUT):
	@if not exist "$(OUT)" mkdir "$(OUT)"

run:
	@$(MAKE) -f Client.mk --no-print-directory run

runJar:
	@$(MAKE) -f Client.mk --no-print-directory runJar

burn:
	@$(MAKE) -f Applet.mk --no-print-directory burn

jar:
	@$(MAKE) -f Client.mk --no-print-directory jar