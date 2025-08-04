#!/bin/bash


echo "Moving domain improvement backlog issues to GitHub project..."
echo "Target Project: https://github.com/orgs/JuniperaDev/projects/2"
echo "Repository: JuniperaDev/erp-system"
echo "Issues to move: #46-69 (24 total issues)"
echo ""

PROJECT_NUMBER=2
OWNER="JuniperaDev"
REPO="JuniperaDev/erp-system"

TOTAL_ISSUES=24
CURRENT=0

echo "=== MOVING DOMAIN IMPROVEMENT EPIC ISSUES (#46-49) ==="
echo ""

for i in {46..49}; do
    CURRENT=$((CURRENT + 1))
    echo "[$CURRENT/$TOTAL_ISSUES] Adding Domain Epic issue #$i to project..."
    gh project item-add $PROJECT_NUMBER --owner $OWNER --url "https://github.com/$REPO/issues/$i"
    if [ $? -eq 0 ]; then
        echo "✅ Successfully added issue #$i"
    else
        echo "❌ Failed to add issue #$i"
    fi
    echo ""
done

echo "=== MOVING DOMAIN IMPROVEMENT USER STORY ISSUES (#50-69) ==="
echo ""

for i in {50..69}; do
    CURRENT=$((CURRENT + 1))
    echo "[$CURRENT/$TOTAL_ISSUES] Adding Domain User Story issue #$i to project..."
    gh project item-add $PROJECT_NUMBER --owner $OWNER --url "https://github.com/$REPO/issues/$i"
    if [ $? -eq 0 ]; then
        echo "✅ Successfully added issue #$i"
    else
        echo "❌ Failed to add issue #$i"
    fi
    echo ""
done

echo "=== DOMAIN IMPROVEMENT MIGRATION COMPLETE ==="
echo ""
echo "Summary:"
echo "- Total issues moved: $TOTAL_ISSUES"
echo "- Epic issues: #46-49 (4 issues)"
echo "  - Epic D1: Reduce Relationship Complexity (#46)"
echo "  - Epic D2: Implement Domain Events (#47)"
echo "  - Epic D3: Performance Optimization (#48)"
echo "  - Epic D4: Bounded Context Separation (#49)"
echo "- User story issues: #50-69 (20 issues)"
echo "- Target project: https://github.com/orgs/JuniperaDev/projects/2"
echo ""
echo "Note: Migration strategy issues (#2-45) were NOT moved and remain separate."
echo ""

echo "=== VERIFICATION COMMANDS ==="
echo ""
echo "To verify the migration was successful, run these commands:"
echo ""
echo "1. List all issues in the project:"
echo "   gh project view $PROJECT_NUMBER --owner $OWNER"
echo ""
echo "2. Check domain improvement issues are in project:"
echo "   gh issue list --repo $REPO --limit 50 --state open --label \"domain-improvement\" --json number,title | jq '.[] | {number, title}'"
echo ""
echo "3. Verify migration strategy issues remain separate:"
echo "   gh issue list --repo $REPO --limit 50 --state open --json number,title | jq '.[] | select(.number >= 2 and .number <= 45) | {number, title}'"
echo ""
echo "4. Check specific domain improvement phases:"
echo "   echo \"Phase 1 - Relationship Complexity:\""
echo "   gh issue list --repo $REPO --state open --label \"relationship-complexity\" --json number,title | jq '.[] | {number, title}'"
echo ""
echo "   echo \"Phase 2 - Domain Events:\""
echo "   gh issue list --repo $REPO --state open --label \"domain-events\" --json number,title | jq '.[] | {number, title}'"
echo ""
echo "   echo \"Phase 3 - Performance Optimization:\""
echo "   gh issue list --repo $REPO --state open --label \"performance\" --json number,title | jq '.[] | {number, title}'"
echo ""
echo "   echo \"Phase 4 - Bounded Context Separation:\""
echo "   gh issue list --repo $REPO --state open --label \"bounded-context\" --json number,title | jq '.[] | {number, title}'"
echo ""
echo "Domain improvement migration script completed!"
