.exec(session => {
session.set("sign", SignatureGeneration.getSignature(key, ElFileBody("abc.json").toString()))
                      })

abc.json -
{"device": "${device}"}
