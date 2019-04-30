#!/bin/bash
echo "$(date): starting copy"
scp out/artifacts/SCR_jar/SCR.jar ev3:/home/lejos/programs
echo "$(date): copy done"