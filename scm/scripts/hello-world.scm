;;; Hello world, in Scheme.
;;;
;;; Writes a hello world message on the console.  First, uses scheme's
;;; native output facility.  Then uses jScheme's javadot notation to
;;; invoke Java library function to do the same.
;;;
;;; Author:
;;;   Filip Miletic (filmil@gmail.com)

(define hello-world "Hello world")

;; Greetings, the scheme way.
(display hello-world)
(newline)

;; jScheme's javadot notation, see more at
;; http://jscheme.sf.net/jscheme/doc/javadot.html
(.println System.out$ hello-world)
(exit)				   ; Required to exit the interpreter.
