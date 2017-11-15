DIR=implementation
default:
	mkdir -p $(DIR)
	cp android/app/build/outputs/apk/app-debug.apk $(DIR)
	cp server/out/artifacts/ticket_jar/ticket.jar $(DIR)
	zip -r implementation.zip $(DIR)
