#!/bin/bash


echo "Moving microservices migration strategy backlog issues to GitHub project..."
echo "Target Project: https://github.com/orgs/JuniperaDev/projects/2"
echo "Repository: JuniperaDev/erp-system"
echo "Issues to move: #2-45 (44 total issues)"
echo ""

PROJECT_NUMBER=2
OWNER="JuniperaDev"
REPO="JuniperaDev/erp-system"

TOTAL_ISSUES=44
CURRENT=0

echo "=== MOVING EPIC ISSUES (#2-12) ==="
echo ""

for i in {2..12}; do
    CURRENT=$((CURRENT + 1))
    echo "[$CURRENT/$TOTAL_ISSUES] Adding Epic issue #$i to project..."
    gh project item-add $PROJECT_NUMBER --owner $OWNER --url "https://github.com/$REPO/issues/$i"
    if [ $? -eq 0 ]; then
        echo "✅ Successfully added issue #$i"
    else
        echo "❌ Failed to add issue #$i"
    fi
    echo ""
done

echo "=== MOVING USER STORY ISSUES (#13-45) ==="
echo ""

for i in {13..45}; do
    CURRENT=$((CURRENT + 1))
    echo "[$CURRENT/$TOTAL_ISSUES] Adding User Story issue #$i to project..."
    gh project item-add $PROJECT_NUMBER --owner $OWNER --url "https://github.com/$REPO/issues/$i"
    if [ $? -eq 0 ]; then
        echo "✅ Successfully added issue #$i"
    else
        echo "❌ Failed to add issue #$i"
    fi
    echo ""
done

echo "=== MIGRATION COMPLETE ==="
echo ""
echo "Summary:"
echo "- Total issues moved: $TOTAL_ISSUES"
echo "- Epic issues: #2-12 (11 issues)"
echo "- User story issues: #13-45 (33 issues)"
echo "- Target project: https://github.com/orgs/JuniperaDev/projects/2"
echo ""
echo "Note: Domain improvement issues (#46-69) were NOT moved and remain separate."
echo ""

echo "=== VERIFICATION COMMANDS ==="
echo ""
echo "To verify the migration was successful, run these commands:"
echo ""
echo "1. List all issues in the project:"
echo "   gh project view $PROJECT_NUMBER --owner $OWNER"
echo ""
echo "2. Check migration strategy issues are in project:"
echo "   gh issue list --repo $REPO --limit 50 --state open --json number,title | jq '.[] | select(.number >= 2 and .number <= 45) | {number, title}'"
echo ""
echo "3. Verify domain improvement issues remain separate:"
echo "   gh issue list --repo $REPO --limit 50 --state open --label \"domain-improvement\" --json number,title | jq '.[] | {number, title}'"
echo ""
echo "Migration script completed!"
