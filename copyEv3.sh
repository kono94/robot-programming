#!/bin/bash
echo "$(date): starting copy"
scp out/artifacts/scr/scr.jar ev3:/home/lejos/programs
echo "$(date): copy done"